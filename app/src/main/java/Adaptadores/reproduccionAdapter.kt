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
import com.example.controlgana.EditarReproduccionActivity
import com.example.controlgana.R

class ReproduccionAdapter(
    private val context: Context,
    private val datosReproduccion: MutableList<org.json.JSONObject>
) : RecyclerView.Adapter<ReproduccionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.reproduccion_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datosReproduccion[position])
    }

    override fun getItemCount(): Int = datosReproduccion.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tv_id)
        private val tvAnimal: TextView = itemView.findViewById(R.id.tv_animal)
        private val tvFechaMate: TextView = itemView.findViewById(R.id.tv_fecha_mate)
        private val tvResultado: TextView = itemView.findViewById(R.id.tv_resultado)
        private val tvObservaciones: TextView = itemView.findViewById(R.id.tv_observaciones)
        private val btnEliminar: Button = itemView.findViewById(R.id.btn_Eliminar_reproduccion)
        private val btnActualizar: Button = itemView.findViewById(R.id.btn_editar_reproduccion)

        init {
            // Eliminar con dialogo de confirmacion
            itemView.setOnLongClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    confirmarYEliminar(pos)
                }
                true
            }
            // Boton actualizar
            btnActualizar.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val datos = datosReproduccion[pos]
                    val intent = Intent(context, EditarReproduccionActivity::class.java).apply {
                        putExtra("id", datos.getString("id"))
                        putExtra("animal_id", datos.getString("animal_id"))
                        putExtra("fecha_mate", datos.getString("fecha_mate"))
                        putExtra("resultado", datos.getString("resultado"))
                        putExtra("observaciones", datos.getString("observaciones"))
                    }
                    context.startActivity(intent)
                }
            }
        }

        fun bind(datos: org.json.JSONObject) {
            tvId.text = datos.getString("id")
            tvAnimal.text = datos.getString("animal_id")
            tvFechaMate.text = datos.getString("fecha_mate")
            tvResultado.text = datos.getString("resultado")
            tvObservaciones.text = datos.getString("observaciones")

            btnEliminar.setOnClickListener {
                val pos = adapterPosition
                if (pos != RecyclerView.NO_POSITION) confirmarYEliminar(pos)
            }
        }

        private fun confirmarYEliminar(pos: Int) {
            AlertDialog.Builder(context)
                .setTitle("Eliminar reproducción")
                .setMessage("¿Seguro que quieres eliminar este registro de reproducción?")
                .setPositiveButton("Sí") { dialog, _ ->
                    eliminarDelServidor(pos)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
                .show()
        }

        private fun eliminarDelServidor(pos: Int) {
            val idRepro = datosReproduccion[pos].getString("id")
            val url = "http://192.168.1.249/phpProyecto/eliminar_reproduccion.php"
            Volley.newRequestQueue(context).add(
                object : StringRequest(Request.Method.POST, url,
                    { response ->
                        if (response.contains("éxito", true) || response.contains("OK", true)) {
                            datosReproduccion.removeAt(pos)
                            notifyItemRemoved(pos)
                            Toast.makeText(context, "Reproducción eliminada", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error: $response", Toast.LENGTH_LONG).show()
                        }
                    },
                    { error -> Toast.makeText(context, "Error de red: ${error.message}", Toast.LENGTH_LONG).show() }
                ) {
                    override fun getParams(): MutableMap<String, String> = hashMapOf("id" to idRepro)
                }
            )
        }
    }
}
