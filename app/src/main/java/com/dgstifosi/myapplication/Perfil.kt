package com.dgstifosi.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth

class Perfil : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil) // Aquí debe ir

        enableEdgeToEdge()

        // Verificar si el usuario está autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this, Entrada::class.java)
            startActivity(intent)
            finish()
            return
        }

        val botonEntrada = findViewById<Button>(R.id.backLogin4)
        botonEntrada.setOnClickListener {
            val intent = Intent(this, Entrada::class.java)
            startActivity(intent)
            finish()
        }

        val botoncontenido = findViewById<Button>(R.id.Contenido)
        botoncontenido.setOnClickListener {
            val intent = Intent(this, Cambiops::class.java)
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}