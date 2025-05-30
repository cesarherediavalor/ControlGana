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

class add_reproduccion : AppCompatActivity() {

    private lateinit var spinnerAnimal: Spinner
    private lateinit var edFechaMate: EditText
    private lateinit var spinnerResultado: Spinner
    private lateinit var edObservaciones: EditText
    private lateinit var btnAddReproduccion: Button

    private lateinit var reproList: List<Pair<Int,String>>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_reproduccion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        spinnerAnimal       = findViewById(R.id.spinner_animal)
        edFechaMate         = findViewById(R.id.et_FechaMate)
        spinnerResultado    = findViewById(R.id.spinner_resultado)
        edObservaciones     = findViewById(R.id.et_Observaciones)
        btnAddReproduccion  = findViewById(R.id.btn_add_reproduccion)

        // 1) llenar Spinner de animales
        cargarAnimales(this, spinnerAnimal) { list ->
            reproList = list
        }

        // 2) llenar Spinner de resultados (Exitoso / Fallido)
        val resultados = listOf("Exitoso", "Fallido")
        spinnerResultado.adapter = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, resultados
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        btnAddReproduccion.setOnClickListener {
            // validar selección animal
            val pos = spinnerAnimal.selectedItemPosition
            if (!::reproList.isInitialized || pos < 0 || pos >= reproList.size) {
                Toast.makeText(this, "Selecciona un animal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val animalId = reproList[pos].first.toString()

            // validar fecha
            val fecha = edFechaMate.text.toString().trim()
            if (fecha.isEmpty()) {
                Toast.makeText(this, "Ingresa la fecha de apareamiento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // resultado
            val resultado = spinnerResultado.selectedItem.toString()

            // observaciones
            val obs = edObservaciones.text.toString().trim()
            if (obs.isEmpty()) {
                Toast.makeText(this, "Agrega observaciones", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // enviamos al servidor
            agregarReproduccion(animalId, fecha, resultado, obs)
        }
    }

    /** Carga lista de animales para asociarlos */
    private fun cargarAnimales(
        context: Context,
        spinner: Spinner,
        callback: (List<Pair<Int,String>>) -> Unit
    ) {
        val url = "http://192.168.1.249/phpProyecto/animales.php"
        Volley.newRequestQueue(context).add(
            JsonArrayRequest(Request.Method.GET, url, null,
                { resp ->
                    val list = mutableListOf<Pair<Int,String>>()
                    val names = mutableListOf<String>()
                    for (i in 0 until resp.length()) {
                        val obj = resp.getJSONObject(i)
                        list += (obj.getInt("animal_id") to obj.getString("nombre"))
                        names += obj.getString("nombre")
                    }
                    spinner.adapter = ArrayAdapter(
                        context, android.R.layout.simple_spinner_item, names
                    ).also {
                        it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    }
                    callback(list)
                },
                { err ->
                    Toast.makeText(context, "Error al cargar animales", Toast.LENGTH_LONG).show()
                    callback(emptyList())
                }
            )
        )
    }

    /** Inserta el registro de reproducción */
    private fun agregarReproduccion(
        animalId: String,
        fechaMate: String,
        resultado: String,
        observaciones: String
    ) {
        val url = "http://192.168.1.249/phpProyecto/add_reproduccion.php"
        Volley.newRequestQueue(this).add(
            object : StringRequest(Request.Method.POST, url,
                Response.Listener { resp ->
                    Toast.makeText(this, resp, Toast.LENGTH_LONG).show()
                    finish()
                },
                Response.ErrorListener { err ->
                    err.networkResponse?.let { nr ->
                        val body = String(nr.data, Charsets.UTF_8)
                        Toast.makeText(this,
                            "HTTP ${nr.statusCode}: $body",
                            Toast.LENGTH_LONG).show()
                        Log.e("VOLLEY_REPRO", "Status ${nr.statusCode}, body: $body")
                    } ?: run {
                        Toast.makeText(this,
                            "Error red: ${err.message}",
                            Toast.LENGTH_LONG).show()
                        Log.e("VOLLEY_REPRO", err.toString())
                    }
                }
            ) {
                override fun getParams(): MutableMap<String,String> = hashMapOf(
                    "animal_id"    to animalId,
                    "fecha_mate"   to fechaMate,
                    "resultado"    to resultado,
                    "observaciones" to observaciones
                )
            }
        )
    }
}
