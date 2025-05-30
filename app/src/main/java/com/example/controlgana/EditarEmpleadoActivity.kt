package com.example.controlgana

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EditarEmpleadoActivity  : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCargo: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnGuardar: Button

    private var idEmpleado: String? = null  // para guardar el ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_empleado)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etCargo = findViewById(R.id.etCargo)
        etTelefono = findViewById(R.id.etTelefono)
        btnGuardar = findViewById(R.id.btnGuardar)

        // Recibir datos del Intent
        idEmpleado = intent.getStringExtra("id")
        etNombre.setText(intent.getStringExtra("nombre"))
        etApellido.setText(intent.getStringExtra("apellido"))
        etCargo.setText(intent.getStringExtra("cargo"))
        etTelefono.setText(intent.getStringExtra("telefono"))

        // Acción del botón
        btnGuardar.setOnClickListener {
            actualizarEmpleado()
        }
    }

    private fun actualizarEmpleado() {
        val url = "http://192.168.1.249/phpProyecto/actualizar_empleado.php"

        val queue = Volley.newRequestQueue(this)

        val request = object : StringRequest(Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Empleado actualizado exitosamente", Toast.LENGTH_SHORT).show()
                finish() // cerrar la actividad después de actualizar
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["id"] = idEmpleado ?: ""
                params["nombre"] = etNombre.text.toString()
                params["apellido"] = etApellido.text.toString()
                params["cargo"] = etCargo.text.toString()
                params["telefono"] = etTelefono.text.toString()
                return params
            }
        }
        queue.add(request)
    }

}