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
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.controlgana.EditarAlimentacionActivity

import com.example.controlgana.R

class AlimentacionAdapter(
    private val context: Context,
    private val datosAlimento: MutableList<org.json.JSONObject>
) : RecyclerView.Adapter<AlimentacionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alimentacion_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datosAlimento[position])
    }

    override fun getItemCount(): Int = datosAlimento.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tv_id)
        private val tvAnimal: TextView = itemView.findViewById(R.id.tv_animal)
        private val tvFecha: TextView = itemView.findViewById(R.id.tv_fecha)
        private val tvTipo: TextView = itemView.findViewById(R.id.tv_tipo_alimento)
        private val tvCantidad: TextView = itemView.findViewById(R.id.tv_cantidad)
        private val btnEliminar: Button = itemView.findViewById(R.id.btn_Eliminar_alimentacion)
        private val btnActualizar: Button = itemView.findViewById(R.id.btn_editar_alimentacion)

        init {
            // Eliminar con diálogo
            itemView.setOnLongClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) confirmarYEliminar(pos)
                true
            }

            btnActualizar.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val datos = datosAlimento[pos]
                    val intent = Intent(context, EditarAlimentacionActivity::class.java).apply {
                        putExtra("id", datos.getString("id"))
                        putExtra("animal_id", datos.getString("animal_id"))
                        putExtra("fecha", datos.getString("fecha"))
                        putExtra("tipo_alimento", datos.getString("tipo_alimento"))
                        putExtra("cantidad", datos.getString("cantidad"))
                    }
                    context.startActivity(intent)
                }
            }
        }

        fun bind(datos: org.json.JSONObject) {
            tvId.text = datos.getString("id")
            tvAnimal.text = datos.getString("animal_id")
            tvFecha.text = datos.getString("fecha")
            tvTipo.text = datos.getString("tipo_alimento")
            tvCantidad.text = datos.getString("cantidad")

            btnEliminar.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) confirmarYEliminar(pos)
            }
        }

        private fun confirmarYEliminar(pos: Int) {
            AlertDialog.Builder(context)
                .setTitle("Eliminar alimentación")
                .setMessage("¿Seguro que quieres eliminar este registro de alimentación?")
                .setPositiveButton("Sí") { dialog, _ ->
                    eliminarDelServidor(pos)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { d, _ -> d.dismiss() }
                .show()
        }

        private fun eliminarDelServidor(pos: Int) {
            val idAlim = datosAlimento[pos].getString("id")
            val url = "http://192.168.1.249/phpProyecto/eliminar_alimentacion.php"
            Volley.newRequestQueue(context).add(
                object : StringRequest(Request.Method.POST, url,
                    { response ->
                        if (response.contains("éxito", true) || response.contains("OK", true)) {
                            datosAlimento.removeAt(pos)
                            notifyItemRemoved(pos)
                            Toast.makeText(context, "Alimentación eliminada", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error: $response", Toast.LENGTH_LONG).show()
                        }
                    },
                    { error -> Toast.makeText(context, "Error red: ${error.message}", Toast.LENGTH_LONG).show() }
                ) {
                    override fun getParams(): MutableMap<String, String> = hashMapOf("id" to idAlim)
                }
            )
        }
    }
}
