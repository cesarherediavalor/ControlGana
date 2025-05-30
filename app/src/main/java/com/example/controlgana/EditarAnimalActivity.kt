package com.example.controlgana



import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditarAnimalActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etRaza: EditText
    private lateinit var etFecha: EditText
    private lateinit var etSexo: EditText
    private lateinit var etPeso: EditText
    private lateinit var etSalud: EditText
    private lateinit var btnGuardar: Button

    private var idAnimal: String? = null  // para guardar el ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_animal)

        etNombre = findViewById(R.id.etNombre)
        etRaza = findViewById(R.id.etRaza)
        etFecha = findViewById(R.id.etFecha)
        etSexo = findViewById(R.id.etSexo)
        etPeso = findViewById(R.id.etPeso)
        etSalud = findViewById(R.id.etSalud)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Recibir datos del Intent
        idAnimal = intent.getStringExtra("id")
        etNombre.setText(intent.getStringExtra("nombre"))
        etRaza.setText(intent.getStringExtra("raza"))
        etFecha.setText(intent.getStringExtra("fecha_nacimiento"))
        etSexo.setText(intent.getStringExtra("sexo"))
        etPeso.setText(intent.getStringExtra("peso"))
        etSalud.setText(intent.getStringExtra("estado_salud"))

        // Acción del botón
        btnGuardar.setOnClickListener {
            actualizarAnimal()
        }
    }

    private fun actualizarAnimal() {
        val url = "http://192.168.1.249/phpProyecto/actualizar_animal.php"

        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Animal actualizado exitosamente", Toast.LENGTH_SHORT).show()
                finish() // cerrar la actividad después de actualizar
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["animal_id"] = idAnimal ?: ""
                params["nombre"] = etNombre.text.toString()
                params["raza"] = etRaza.text.toString()
                params["fecha_nacimiento"] = etFecha.text.toString()
                params["sexo"] = etSexo.text.toString()
                params["peso"] = etPeso.text.toString()
                params["estado_salud"] = etSalud.text.toString()
                return params
            }
        }
        queue.add(request)
    }
}
