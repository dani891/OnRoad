package com.dgstifosi.myapplication

import android.annotation.SuppressLint
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
import com.google.firebase.auth.FirebaseAuth

class Forgotpassword : AppCompatActivity() {

    private lateinit var botoncambio:Button
    private lateinit var botonEntrada:Button
    private lateinit var email :EditText
    private lateinit var recuperar:Button

    private lateinit var auth: FirebaseAuth
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgotpassword)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(com.dgstifosi.myapplication.R.id.emailfp)
        val recuperar =findViewById<Button>(com.dgstifosi.myapplication.R.id.Recuperarcontraseña)
        recuperar.setOnClickListener{
            val correo =email.text.toString().trim()

            if (correo.isNotEmpty()) {
                // Llamada para enviar el correo de recuperación
                sendPasswordResetEmail(correo)
            } else {
                Toast.makeText(this, "Por favor ingresa un correo electrónico", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendPasswordResetEmail(correo: String) {
        auth.sendPasswordResetEmail(correo)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = task.exception?.message ?: "Ocurrió un error"
                    Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
          val systemBars: Insets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonEntrada = findViewById<Button>(com.dgstifosi.myapplication.R.id.backLogin2)
        botonEntrada.setOnClickListener {
            // Crear un Intent para iniciar ForgotPasswordActivity
            val intent = Intent(this, Entrada::class.java)
            startActivity(intent)
            finish()
        }

    }
}
