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

class EmpleadoAdapter(
    private val context: Context,
    private val datosempleado: MutableList<JSONObject>
):
    RecyclerView.Adapter<EmpleadoAdapter.ViewHolder>()  {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpleadoAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.empleado_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: EmpleadoAdapter.ViewHolder, position: Int) {
        val datos = datosempleado[position]
        holder.bind(datos)
    }

    override fun getItemCount(): Int = datosempleado.size




    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)    {
        private val tvId:TextView =itemView.findViewById(R.id.tv_id)//1
        private val tvNombre:TextView =itemView.findViewById(R.id.tv_nombre)
        private val tvApellido:TextView =itemView.findViewById(R.id.tvApellido)//1
        private val tvCargo:TextView =itemView.findViewById(R.id.tvCargo)//1
        private val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
        private val btnEliminar: Button = itemView.findViewById(R.id.btn_Eliminar_empleado)
        private val btnActualizar: Button = itemView.findViewById(R.id.btn_editar_empleado)

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
            tvNombre.text=datos.getString("nombre")
            tvApellido.text=datos.getString("apellido")
            tvCargo.text=datos.getString("cargo")
            tvTelefono.text=datos.getString("telefono")

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
            val idEmpleado = datosempleado[pos].getString("id")
            // Ajusta esta URL a tu endpoint real:
            val url = "http://192.168.1.249/phpProyecto/eliminar_empleado.php"
            val queue = Volley.newRequestQueue(context)
            val req = object : StringRequest(
                Request.Method.POST, url,
                { response ->
                    if (response.contains("éxito", true) || response.contains("OK", true)) {
                        // Lo borramos localmente
                        datosempleado.removeAt(pos)
                        notifyItemRemoved(pos)
                        Toast.makeText(context, "Empleado eliminado", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: $response", Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    Toast.makeText(context, "Error de red: ${error.message}", Toast.LENGTH_LONG).show()
                }
            ) {
                override fun getParams(): MutableMap<String, String> {
                    return mutableMapOf("id" to idEmpleado)
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
                    val datos = datosempleado[position]
                    val intent = Intent(context, EditarEmpleadoActivity ::class.java)
                    intent.putExtra("id", datos.getString("id"))
                    intent.putExtra("nombre", datos.getString("nombre"))
                    intent.putExtra("apellido", datos.getString("apellido"))
                    intent.putExtra("cargo", datos.getString("cargo"))
                    intent.putExtra("telefono", datos.getString("telefono"))
                    context.startActivity(intent)
                }
            }
        }


    }


}