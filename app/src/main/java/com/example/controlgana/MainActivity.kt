package com.example.controlgana

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    //declaracion de variables de card
    private lateinit var cardAnimal: CardView

    private lateinit var cardEmpleados: CardView

    private lateinit var cardSalud: CardView

    private lateinit var cardReproduccion: CardView

    private lateinit var cardAlimentacion: CardView




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //iniciar vardview
        cardAnimal=findViewById(R.id.card_animales)

        //configuracion del onclicklistener
        cardAnimal.setOnClickListener{
            val ventanaAnimales= Intent(this, view_animal::class.java)
            startActivity(ventanaAnimales)
        }

        //iniciar vardview
        cardEmpleados=findViewById(R.id.card_empleados)

        //configuracion del onclicklistener
        cardEmpleados.setOnClickListener{
            val ventanaEmpleados= Intent(this, view_empleado::class.java)
            startActivity(ventanaEmpleados)
        }

        //iniciar vardview
        cardSalud=findViewById(R.id.card_salud)

        //configuracion del onclicklistener
        cardSalud.setOnClickListener{
            val ventanaSalud= Intent(this, view_salud::class.java)
            startActivity(ventanaSalud)
        }

        //iniciar vardview
        cardReproduccion=findViewById(R.id.card_reproduccion)

        //configuracion del onclicklistener
        cardReproduccion.setOnClickListener{
            val ventanaReproduccion= Intent(this, view_reproduccion::class.java)
            startActivity(ventanaReproduccion)
        }

        //iniciar vardview
        cardAlimentacion=findViewById(R.id.card_alimentacion)

        //configuracion del onclicklistener
        cardAlimentacion.setOnClickListener{
            val ventanaReproduccion= Intent(this, view_alimentacion::class.java)
            startActivity(ventanaReproduccion)
        }
    }
}