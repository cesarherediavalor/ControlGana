package com.example.controlgana

import Adaptadores.EmpleadoAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONObject

class view_empleado : AppCompatActivity() {

    //PARTE DE LAS VISTAS
    lateinit var recyblerView: RecyclerView //VISTAS DE EMPLEADOS
    //INICIAR EL ADAPTADOR
    lateinit var adaptador: EmpleadoAdapter
    // Lista mutable de JSONObjects con los datos de empleados
    private val listaEmpleados = mutableListOf<JSONObject>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_empleado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyblerView = findViewById(R.id.listaEmpleados)
        recyblerView.layoutManager = LinearLayoutManager(this)

        // Crear el adapter con la lista vacía inicialmente
        adaptador = EmpleadoAdapter(this, listaEmpleados)
        recyblerView.adapter = adaptador

        // Cargar animales desde el servidor y actualizar la lista
        cargarEmpleado()



        // Botón flotante para agregar un nuevo empleado
        val addbotonP: FloatingActionButton = findViewById(R.id.addEmpleados)
        addbotonP.setOnClickListener {
            val addAnimalIntent = Intent(this, add_empleado::class.java)
            startActivity(addAnimalIntent)
            Toast.makeText(this, "Presionaste Agregar", Toast.LENGTH_SHORT).show()
        }

    }



    /**
     * Solicita al servidor la lista de empleados en formato JSON
     * y actualiza la lista del adapter.
     */
    private fun cargarEmpleado() {
        val url = "http://192.168.1.249/phpProyecto/empleados.php"
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                // Limpiar lista y agregar nuevos datos
                listaEmpleados.clear()
                for (i in 0 until response.length()) {
                    listaEmpleados.add(response.getJSONObject(i))
                }
                // Notificar al adapter que los datos cambiaron
                adaptador.notifyDataSetChanged()
            },
            { error ->
                // Error al obtener datos
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar empleados: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
    }

}


