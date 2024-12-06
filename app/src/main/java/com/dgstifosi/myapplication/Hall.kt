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

private lateinit var auth: FirebaseAuth
class Hall : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_hall)

        // Iniciamos Firebase auth
        val auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val butoncerrar = findViewById<Button>(com.dgstifosi.myapplication.R.id.butclose)
        butoncerrar.setOnClickListener {
            // Cierra la sesión del usuario
            auth.signOut()// Redirige al inicio 
            val intent = Intent(this, Entrada::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Abre nueva vista con el mapa de ruta
            val butonplan = findViewById<Button>(com.dgstifosi.myapplication.R.id.butplan)
        butonplan.setOnClickListener { // Acción al hacer clic
            val intent = Intent(this, Navegador::class.java)
            startActivity(intent)

        }
        // Abre nueva vista Perfil del Usuario
        val butonperfil = findViewById<Button>(com.dgstifosi.myapplication.R.id.butper)
        butonperfil.setOnClickListener { // Acción al hacer clic
            val intent = Intent(this, Perfil::class.java)
            startActivity(intent)

        }

    }
}