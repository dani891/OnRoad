package com.dgstifosi.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dgstifosi.myapplication.R.id.main
import com.google.firebase.auth.FirebaseAuth


class Hall : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hall)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val butonplan = findViewById<Button>(com.dgstifosi.myapplication.R.id.butplan)
        butonplan.setOnClickListener { // Acción al hacer clic
            val intent = Intent(this, Navegador::class.java)
            startActivity(intent)

        }
        // Botón de cerrar sesión
        val botonLogout = findViewById<Button>(R.id.butclose)
        botonLogout.setOnClickListener {
            logOutUser() // Llama a la función que cierra sesión
        }
    }

    // Función para cerrar sesión
    private fun logOutUser() {
        // Cerrar sesión en Firebase
        FirebaseAuth.getInstance().signOut()

        // Redirigir al login después de cerrar sesión
        val intent = Intent(this, Entrada::class.java)
        startActivity(intent)
        finish() // Asegura que no se pueda regresar a la actividad anterior
    }
}