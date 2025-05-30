package Adaptadores


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import org.json.JSONObject
import  android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.controlgana.EditarAnimalActivity
import com.example.controlgana.EditarEmpleadoActivity
import com.example.controlgana.R
import com.example.controlgana.view_empleado


class SaludAdapter (
    private val context: Context,
    private val datossalud: MutableList<JSONObject>
):
    RecyclerView.Adapter<SaludAdapter.ViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SaludAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.salud_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val datos = datossalud[position]
        holder.bind(datos)
    }

    override fun getItemCount(): Int = datossalud.size


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)    {
        private val tvId:TextView =itemView.findViewById(R.id.tv_id)//1
        private val tvAnimal:TextView =itemView.findViewById(R.id.tv_animal)
        private val tvFechaSalud:TextView =itemView.findViewById(R.id.tvFecha)//1
        private val tvDescripcion:TextView =itemView.findViewById(R.id.tvDescripcion)//1
        private val tvTratamiento: TextView = itemView.findViewById(R.id.tvTratamiento)
        private val btnEliminar: Button = itemView.findViewById(R.id.btn_Eliminar_salud)
        private val btnActualizar: Button = itemView.findViewById(R.id.btn_editar_salud)

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

        fun bind(datos: JSONObject){
            tvId.text=datos.getString("id")//1
            tvAnimal.text=datos.getString("animal_id")
            tvFechaSalud.text=datos.getString("fecha")
            tvDescripcion.text=datos.getString("descripcion")
            tvTratamiento.text=datos.getString("tratamiento")

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
                .setTitle("Eliminar salud")
                .setMessage("¿Seguro que quieres eliminar esta salud?")
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
            val idSalud = datossalud[pos].getString("id")
            // Ajusta esta URL a tu endpoint real:
            val url = "http://192.168.1.249/phpProyecto/eliminar_salud.php"
            val queue = Volley.newRequestQueue(context)
            val req = object : StringRequest(
                Request.Method.POST, url,
                { response ->
                    if (response.contains("éxito", true) || response.contains("OK", true)) {
                        // Lo borramos localmente
                        datossalud.removeAt(pos)
                        notifyItemRemoved(pos)
                        Toast.makeText(context, "Salud eliminado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: $response", Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    Toast.makeText(context, "Error de red: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    return mutableMapOf("id" to idSalud)
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
                    val datos = datossalud[position]
                    val intent = Intent(context, EditarEmpleadoActivity ::class.java)
                    intent.putExtra("id", datos.getString("id"))
                    intent.putExtra("animal_id", datos.getString("animal_id"))
                    intent.putExtra("fecha", datos.getString("fecha"))
                    intent.putExtra("descripcion", datos.getString("descripcion"))
                    intent.putExtra("tratamiento", datos.getString("tratamiento"))
                    context.startActivity(intent)
                }
            }
        }


    }









}