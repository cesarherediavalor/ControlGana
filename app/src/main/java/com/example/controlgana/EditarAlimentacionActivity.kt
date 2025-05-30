package com.example.controlgana

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditarAlimentacionActivity : AppCompatActivity() {

    private lateinit var etAnimalId: EditText
    private lateinit var etFecha: EditText
    private lateinit var etTipoAlimento: EditText
    private lateinit var etCantidad: EditText
    private lateinit var btnGuardar: Button

    private var idAlimentacion: String? = null  // almacena el ID del registro

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_actualizar_alimentacion)

        etAnimalId      = findViewById(R.id.etAnimalId)
        etFecha         = findViewById(R.id.etFecha)
        etTipoAlimento  = findViewById(R.id.etTipoAlimento)
        etCantidad      = findViewById(R.id.etCantidad)
        btnGuardar      = findViewById(R.id.btnGuardar)

        // Recibimos los extras del Intent
        idAlimentacion   = intent.getStringExtra("id")
        etAnimalId.setText(intent.getStringExtra("animal_id"))
        etFecha.setText(intent.getStringExtra("fecha"))
        etTipoAlimento.setText(intent.getStringExtra("tipo_alimento"))
        etCantidad.setText(intent.getStringExtra("cantidad"))

        btnGuardar.setOnClickListener {
            actualizarAlimentacion()
        }
    }

    private fun actualizarAlimentacion() {
        val url = "http://192.168.1.249/phpProyecto/actualizar_alimentacion.php"
        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Alimentación actualizada correctamente", Toast.LENGTH_SHORT).show()
                finish()
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return hashMapOf(
                    "id"             to (idAlimentacion ?: ""),
                    "animal_id"      to etAnimalId.text.toString(),
                    "fecha"          to etFecha.text.toString(),
                    "tipo_alimento"  to etTipoAlimento.text.toString(),
                    "cantidad"       to etCantidad.text.toString()
                )
            }
        }
        queue.add(request)
    }
}
