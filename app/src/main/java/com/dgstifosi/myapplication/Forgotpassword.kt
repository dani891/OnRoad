package com.dgstifosi.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Forgotpassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgotpassword)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
          val systemBars: Insets
            systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val botoncambio = findViewById<Button>(com.dgstifosi.myapplication.R.id.cambio)
        botoncambio.setOnClickListener { // Acción al hacer clic
            val intent = Intent(this, Cambiops::class.java)
            startActivity(intent)
        }
        val botonEntrada = findViewById<Button>(com.dgstifosi.myapplication.R.id.backLogin2)
        botonEntrada.setOnClickListener {
            // Crear un Intent para iniciar ForgotPasswordActivity
            val intent = Intent(this, Entrada::class.java)
            startActivity(intent)
            finish()
        }
        val email = findViewById<EditText>(com.dgstifosi.myapplication.R.id.emailfp)
        val recuperar =findViewById<Button>(com.dgstifosi.myapplication.R.id.Recuperarcontraseña)
        recuperar.setOnClickListener{
            val correo =email.text.toString().trim()

            Toast.makeText(this, "Correo ingresado: $email", Toast.LENGTH_SHORT).show()
        }

    }
}