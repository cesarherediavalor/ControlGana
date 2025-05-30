package com.example.controlgana


import Adaptadores.ReproduccionAdapter
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

class view_reproduccion : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: ReproduccionAdapter
    private val listaRepro = mutableListOf<JSONObject>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_reproduccion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.listaReproduccion)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adaptador = ReproduccionAdapter(this, listaRepro)
        recyclerView.adapter = adaptador

        cargarReproduccion()

        val addButton: FloatingActionButton = findViewById(R.id.addReproduccion)
        addButton.setOnClickListener {
            startActivity(Intent(this, add_reproduccion::class.java))
            Toast.makeText(this, "Presionaste Agregar", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun cargarReproduccion() {
        val url = "http://192.168.1.249/phpProyecto/reproduccion.php"
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                listaRepro.clear()
                for (i in 0 until response.length()) {
                    listaRepro.add(response.getJSONObject(i))
                }
                adaptador.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar reproducción: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
    }
}
