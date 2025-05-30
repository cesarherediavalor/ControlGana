package com.example.controlgana



import Adaptadores.EmpleadoAdapter
import Adaptadores.SaludAdapter
import android.annotation.SuppressLint
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

class view_salud : AppCompatActivity() {

    //PARTE DE LAS VISTAS
    lateinit var recyblerView: RecyclerView //VISTAS DE EMPLEADOS
    //INICIAR EL ADAPTADOR
    lateinit var adaptador: SaludAdapter
    // Lista mutable de JSONObjects con los datos de empleados
    private val listaSalud = mutableListOf<JSONObject>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_salud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        recyblerView = findViewById(R.id.listaSalud)
        recyblerView.layoutManager = LinearLayoutManager(this)

        // Crear el adapter con la lista vacía inicialmente
        adaptador = SaludAdapter(this, listaSalud)
        recyblerView.adapter = adaptador

        // Cargar animales desde el servidor y actualizar la lista
        cargarSalud()



        // Botón flotante para agregar un nuevo empleado
        val addbotonP: FloatingActionButton = findViewById(R.id.addSalud)
        addbotonP.setOnClickListener {
            val addSaludIntent = Intent(this, add_salud::class.java)
            startActivity(addSaludIntent)
            Toast.makeText(this, "Presionaste Agregar", Toast.LENGTH_SHORT).show()
        }

    }


    /**
     * Solicita al servidor la lista de salu en formato JSON
     * y actualiza la lista del adapter.
     */
    private fun cargarSalud() {
        val url = "http://192.168.1.249/phpProyecto/salud.php"
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                // Limpiar lista y agregar nuevos datos
                listaSalud.clear()
                for (i in 0 until response.length()) {
                    listaSalud.add(response.getJSONObject(i))
                }
                // Notificar al adapter que los datos cambiaron
                adaptador.notifyDataSetChanged()
            },
            { error ->
                // Error al obtener datos
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar salud: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
    }

}