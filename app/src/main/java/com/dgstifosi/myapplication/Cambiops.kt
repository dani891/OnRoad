package com.dgstifosi.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Cambiops : AppCompatActivity() {

    //prueba control versiones
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cambiops)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
            val botonEntrada = findViewById<Button>(com.dgstifosi.myapplication.R.id.backLogin3)
            botonEntrada.setOnClickListener {
                // Crear un Intent para volver al inicio
                val intent = Intent(this, Entrada::class.java)
                startActivity(intent)
                finish()
            }
        }
    }