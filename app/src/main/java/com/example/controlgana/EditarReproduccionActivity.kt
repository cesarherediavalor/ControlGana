package com.example.controlgana

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditarReproduccionActivity : AppCompatActivity() {

    private lateinit var etAnimalId: EditText
    private lateinit var etFecha: EditText
    private lateinit var etResultado: EditText
    private lateinit var etObservaciones: EditText
    private lateinit var btnGuardar: Button

    private var idReproduccion: String? = null  // para guardar el ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_reproduccion)

        etAnimalId = findViewById(R.id.etAnimalId)
        etFecha = findViewById(R.id.etFecha)
        etResultado = findViewById(R.id.etResultado)
        etObservaciones = findViewById(R.id.etObservaciones)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Recibir datos del Intent
        idReproduccion = intent.getStringExtra("id")
        etAnimalId.setText(intent.getStringExtra("animal_id"))
        etFecha.setText(intent.getStringExtra("fecha_mate"))
        etResultado.setText(intent.getStringExtra("resultado"))
        etObservaciones.setText(intent.getStringExtra("observaciones"))

        // Acción del botón
        btnGuardar.setOnClickListener {
            actualizarReproduccion()
        }
    }

    private fun actualizarReproduccion() {
        val url = "http://192.168.1.249/phpProyecto/actualizar_reproduccion.php"

        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Reproducción actualizada exitosamente", Toast.LENGTH_SHORT).show()
                finish()
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = idReproduccion ?: ""
                params["animal_id"] = etAnimalId.text.toString()
                params["fecha_mate"] = etFecha.text.toString()
                params["resultado"] = etResultado.text.toString()
                params["observaciones"] = etObservaciones.text.toString()
                return params
            }
        }
        queue.add(request)
    }
}
