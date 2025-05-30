package com.example.controlgana




import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class add_salud : AppCompatActivity() {

    lateinit var edSpinerAnimal : Spinner
    lateinit var edFechaSalud: EditText
    lateinit var edDescripcion: EditText
    lateinit var edTratamiento: EditText
    lateinit var btnaddSalud: Button
    lateinit var btnCacelSalud: Button
    lateinit var spinnerAnimal: Spinner
    lateinit var saludMap: Map<Int, String> // <- corrección en el nombre


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_salud)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edSpinerAnimal = findViewById(R.id.spinner_animal)
        edFechaSalud = findViewById(R.id.et_FechaSalud)
        edDescripcion = findViewById(R.id.et_Descripcion)
        edTratamiento = findViewById(R.id.et_Tratamiento)
        btnaddSalud = findViewById(R.id.btn_add_salud)

        obtenerSalud(this, edSpinerAnimal) { salud ->
            this.saludMap = salud
        }



        btnaddSalud.setOnClickListener {
            // … tus validaciones anteriores …
            val url = "http://192.168.1.249/phpProyecto/add_salud.php"
            val queue = Volley.newRequestQueue(this)
            val request = object : StringRequest(
                Request.Method.POST, url,
                Response.Listener<String> { response ->
                    Toast.makeText(this, response, Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { error ->
                    // Aquí imprimimos más info
                    error.networkResponse?.let { nr ->
                        val body = String(nr.data, Charsets.UTF_8)
                        Toast.makeText(this,
                            "HTTP ${nr.statusCode}: $body",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("VOLLEY", "Status ${nr.statusCode}, body: $body")
                    } ?: run {
                        Toast.makeText(this,
                            error.message ?: "Error desconocido",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("VOLLEY", error.toString())
                    }
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    return mapOf(
                        "animal_id" to edSpinerAnimal.toString(),
                        "fecha" to edFechaSalud.text.toString(),
                        "descripcion" to edDescripcion.text.toString(),
                        "tratamiento"   to edTratamiento.text.toString()
                    ).toMutableMap()
                }
            }
            queue.add(request)
        }
    }

    private fun addSalud(context: Context, animalP: String, fecha: String, descripcion: String, tratamiento: String){
        val url = "http://192.168.1.249/phpProyecto/add_salud.php"
        val peticion = Volley.newRequestQueue(context)

        val stringRequest = object : StringRequest(
            url,
            Response.Listener { response ->
                Toast.makeText(context, response, Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "Error al agregar los datos", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["animal_id"] = animalP
                params["fecha"] = fecha .toString()
                params["descripcion"] = descripcion.toString()
                params["tratamiento"] =  tratamiento.toString()
                return params
            }
        }

        peticion.add(stringRequest)
    }


    private fun obtenerSalud(context: Context, spinner: Spinner, callback: (Map<Int, String>) -> Unit) {
        val url = "http://192.168.1.249/phpweb/animales.php"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val animalMap = mutableMapOf<Int, String>()
                for (i in 0 until response.length()) {
                    val categoriaJson = response.getJSONObject(i)
                    val id = categoriaJson.getInt("animal_id")
                    val nombre = categoriaJson.getString("nombre")
                    animalMap[id] = nombre
                }

                val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, animalMap.values.toList())
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
                callback(animalMap)
            },
            { error ->
                Toast.makeText(context, "Error al obtener los animales", Toast.LENGTH_LONG).show()
                callback(emptyMap())
            }
        )

        Volley.newRequestQueue(context).add(jsonArrayRequest)
    }

}
