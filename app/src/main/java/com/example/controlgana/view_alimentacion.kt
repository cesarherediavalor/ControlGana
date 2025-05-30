package com.example.controlgana

import Adaptadores.AlimentacionAdapter
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

class view_alimentacion : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: AlimentacionAdapter
    private val listaAlim = mutableListOf<JSONObject>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_view_alimentacion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.listaAlimentacion)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adaptador = AlimentacionAdapter(this, listaAlim)
        recyclerView.adapter = adaptador

        cargarAlimentacion()

        val addButton: FloatingActionButton = findViewById(R.id.addAlimentacion)
        addButton.setOnClickListener {
            startActivity(Intent(this, add_alimentacion::class.java))
            Toast.makeText(this, "Presionaste Agregar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarAlimentacion() {
        val url = "http://192.168.1.249/phpProyecto/alimentacion.php"
        val queue = Volley.newRequestQueue(this)
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                listaAlim.clear()
                for (i in 0 until response.length()) {
                    listaAlim.add(response.getJSONObject(i))
                }
                adaptador.notifyDataSetChanged()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error al cargar alimentación: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
        queue.add(request)
    }
}
