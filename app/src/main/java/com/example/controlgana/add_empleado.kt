package com.example.controlgana




import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley


class add_empleado : AppCompatActivity(){

    lateinit var edNombre: EditText
    lateinit var edApellido: EditText
    lateinit var edCargo: EditText
    lateinit var edTelefono: EditText
    lateinit var btnaddEmpleado: Button
    lateinit var btnCacelEmpleado: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_empleado)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        edNombre = findViewById(R.id.et_nombre1)
        edApellido = findViewById(R.id.et_apellido)
        edCargo = findViewById(R.id.et_cargo)
        edTelefono = findViewById(R.id.et_telefono)
        btnaddEmpleado = findViewById(R.id.btn_add_empleado)



        btnaddEmpleado.setOnClickListener {
            // … tus validaciones anteriores …
            val url = "http://192.168.1.249/phpProyecto/add_empleado.php"
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
                        "nombre" to edNombre.text.toString(),
                        "apellido" to edApellido.text.toString(),
                        "cargo" to edCargo.text.toString(),
                        "telefono"   to edTelefono.text.toString()
                    ).toMutableMap()
                }
            }
            queue.add(request)
        }

    }


    private fun addAnimal(context: Context, nombreP: String, apellido: String, cargo: String, telefono: String){
        val url = "http://192.168.1.249/phpProyecto/add_empleado.php"
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
                params["nombre"] = nombreP
                params["apellido"] = apellido.toString()
                params["cargo"] = cargo.toString()
                params["telefono"] =  telefono.toString()
                return params
            }
        }

        peticion.add(stringRequest)
    }





}