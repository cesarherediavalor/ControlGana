package Adaptadores

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.controlgana.R
import org.json.JSONObject
import androidx.recyclerview.widget.RecyclerView
import com.example.controlgana.EditarAnimalActivity


class AnimalAdapter(
    private val context: Context,
    private val datosanimal: MutableList<JSONObject>
) : RecyclerView.Adapter<AnimalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.animal_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datosanimal[position])
    }

    override fun getItemCount(): Int = datosanimal.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tv_id)
        private val tvNombre: TextView = itemView.findViewById(R.id.tv_nombre)
        private val tvRaza: TextView = itemView.findViewById(R.id.tvRaza)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        private val tvSexo: TextView = itemView.findViewById(R.id.tvSexo)
        private val tvPeso: TextView = itemView.findViewById(R.id.tvPeso)
        private val tvSalud: TextView = itemView.findViewById(R.id.tvSalud)
        private val btnEliminar: Button = itemView.findViewById(R.id.btn_Eliminar_animal)
        private val btnActualizar: Button = itemView.findViewById(R.id.btn_editar_animal)


        init {
            // Long click para eliminar
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    confirmarYEliminar(position)
                }
                true
            }
        }

        fun bind(datos: JSONObject) {
            tvId.text = datos.getString("animal_id")
            tvNombre.text = datos.getString("nombre")
            tvRaza.text = datos.getString("raza")
            tvFecha.text = datos.getString("fecha_nacimiento")
            tvSexo.text = datos.getString("sexo")
            tvPeso.text = datos.getString("peso")
            tvSalud.text = datos.getString("estado_salud")

            // 🔥 Agregar click al botón eliminar
            btnEliminar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    confirmarYEliminar(position)
                }
            }
        }

        private fun confirmarYEliminar(pos: Int) {
            AlertDialog.Builder(context)
                .setTitle("Eliminar animal")
                .setMessage("¿Seguro que quieres eliminar este animal?")
                .setPositiveButton("Sí") { dialog, _ ->
                    eliminarDelServidor(pos)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        private fun eliminarDelServidor(pos: Int) {
            val idAnimal = datosanimal[pos].getString("animal_id")
            // Ajusta esta URL a tu endpoint real:
            val url = "http://192.168.1.249/phpProyecto/eliminar_animal.php"
            val queue = Volley.newRequestQueue(context)
            val req = object : StringRequest(
                Request.Method.POST, url,
                { response ->
                    if (response.contains("éxito", true) || response.contains("OK", true)) {
                        // Lo borramos localmente
                        datosanimal.removeAt(pos)
                        notifyItemRemoved(pos)
                        Toast.makeText(context, "Animal eliminado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: $response", Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    Toast.makeText(context, "Error de red: ${error.message}", Toast.LENGTH_LONG)
                        .show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    return mutableMapOf("animal_id" to idAnimal)
                }
            }
            queue.add(req)
        }


        //PARTE DE ACTUALIZAR
        init {
            // Long click para eliminar
            itemView.setOnLongClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    confirmarYEliminar(position)
                }
                true
            }

            // Click en el botón de actualizar
            btnActualizar.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val datos = datosanimal[position]
                    val intent = Intent(context, EditarAnimalActivity::class.java)
                    intent.putExtra("id", datos.getString("animal_id"))
                    intent.putExtra("nombre", datos.getString("nombre"))
                    intent.putExtra("raza", datos.getString("raza"))
                    intent.putExtra("fecha_nacimiento", datos.getString("fecha_nacimiento"))
                    intent.putExtra("sexo", datos.getString("sexo"))
                    intent.putExtra("peso", datos.getString("peso"))
                    intent.putExtra("estado_salud", datos.getString("estado_salud"))
                    context.startActivity(intent)
                }
            }
        }

    }}
