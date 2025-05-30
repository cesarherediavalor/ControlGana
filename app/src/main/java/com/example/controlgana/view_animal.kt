package com.example.controlgana
import Adaptadores.AnimalAdapter
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

class view_animal : AppCompatActivity() {

    // RecyclerView para mostrar lista de animales
    private lateinit var recyclerView: RecyclerView
    // Adapter personalizado para RecyclerView
    private lateinit var adapter: AnimalAdapter
    // Lista mutable de JSONObjects con los datos de animales
    private val listaAnimales = mutableListOf<JSONObject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activty_view_animal)
        // Aplica ajustes de insets para pantallas con barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar RecyclerView y su LayoutManager
        recyclerView = findViewById(R.id.listaAnimal)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear el adapter con la lista vacía inicialmente
        adapter = AnimalAdapter(this, listaAnimales)
        recyclerView.adapter = adapter

        // Cargar animales desde el servidor y actualizar la lista
        cargarAnimales()

        // Botón flotante para agregar un nuevo animal
        val addbotonP: FloatingActionButton = findViewById(R.id.addAnimal)
        addbotonP.setOnClickListener {
            val addAnimalIntent = Intent(this, add_animal::class.java)
            startActivity(addAnimalIntent)
            Toast.makeText(this, "Presionaste Agregar", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Solicita al servidor la lista de animales en formato JSO  N
     * y actualiza la lista del adapter.
     */
    private fun cargarAnimales() {
        val url = "http://192.168.1.249/phpProyecto/animales.php"
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                // Limpiar lista y agregar nuevos datos
                listaAnimales.clear()
                for (i in 0 until response.length()) {
                    listaAnimales.add(response.getJSONObject(i))
                }
                // Notificar al adapter que los datos cambiaron
                adapter.notifyDataSetChanged()
            },
            { error ->
                // Error al obtener datos
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar animales: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )

        queue.add(request)
    }
}
