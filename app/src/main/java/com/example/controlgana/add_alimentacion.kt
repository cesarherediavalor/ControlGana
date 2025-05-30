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

class add_alimentacion : AppCompatActivity() {

    private lateinit var spinnerAnimal: Spinner
    private lateinit var edFecha: EditText
    private lateinit var edTipo: EditText
    private lateinit var edCantidad: EditText
    private lateinit var btnAddAlimentacion: Button

    private lateinit var alimentoList: List<Pair<Int,String>>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_alimentacion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val sys = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(sys.left, sys.top, sys.right, sys.bottom)
            insets
        }

        spinnerAnimal      = findViewById(R.id.spinner_animal)
        edFecha            = findViewById(R.id.et_Fecha)
        edTipo             = findViewById(R.id.et_TipoAlimento)
        edCantidad         = findViewById(R.id.et_Cantidad)
        btnAddAlimentacion = findViewById(R.id.btn_add_alimentacion)

        // 1) llenar Spinner de animales
        cargarAnimales(this, spinnerAnimal) { list ->
            alimentoList = list
        }

        btnAddAlimentacion.setOnClickListener {
            val pos = spinnerAnimal.selectedItemPosition
            if (!::alimentoList.isInitialized || pos < 0 || pos >= alimentoList.size) {
                Toast.makeText(this, "Selecciona un animal", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val animalId = alimentoList[pos].first.toString()

            val fecha = edFecha.text.toString().trim()
            if (fecha.isEmpty()) {
                Toast.makeText(this, "Ingresa la fecha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tipo = edTipo.text.toString().trim()
            if (tipo.isEmpty()) {
                Toast.makeText(this, "Ingresa el tipo de alimento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = edCantidad.text.toString().trim()
            if (cantidad.isEmpty()) {
                Toast.makeText(this, "Ingresa la cantidad", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            agregarAlimentacion(animalId, fecha, tipo, cantidad)
        }
    }

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
                    ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }
                    callback(list)
                },
                { err ->
                    Toast.makeText(context, "Error al cargar animales", Toast.LENGTH_LONG).show()
                    callback(emptyList())
                }
            )
        )
    }

    private fun agregarAlimentacion(
        animalId: String,
        fecha: String,
        tipo: String,
        cantidad: String
    ) {
        val url = "http://192.168.1.249/phpProyecto/add_alimentacion.php"
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
                        Log.e("VOLLEY_ALIM", "Status ${nr.statusCode}, body: $body")
                    } ?: run {
                        Toast.makeText(this,
                            "Error red: ${err.message}",
                            Toast.LENGTH_LONG).show()
                        Log.e("VOLLEY_ALIM", err.toString())
                    }
                }
            ) {
                override fun getParams(): MutableMap<String, String> = hashMapOf(
                    "animal_id"      to animalId,
                    "fecha"          to fecha,
                    "tipo_alimento"  to tipo,
                    "cantidad"       to cantidad
                )
            }
        )
    }
}
