package com.dgstifosi.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth


class Entrada : AppCompatActivity() {

    private lateinit var botonvis: ImageButton
    private lateinit var etPasswor:EditText
    private lateinit var editTextEmail:EditText
    private lateinit var botonregistro:Button
    private lateinit var botonforgot:Button
    private lateinit var botonentrar:Button

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Verificar si el usuario está autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            // Si no hay usuario autenticado, redirigir al login
            val intent = Intent(this, Entrada::class.java)
            startActivity(intent)
            finish() // Asegura que no pueda regresar a esta actividad sin estar autenticado
            return
        }

        setContentView(R.layout.activity_entrada)

        auth = FirebaseAuth.getInstance()

        IniciarVariables()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Prueba control de versiones vers2
        // Acción de el boton crear nueva cuenta al hacer click
        botonregistro.setOnClickListener {
            val intent = Intent(this,Registro::class.java)
            startActivity(intent)

        }
        // Acción de el boton ¿olvidaste tu contraseña? al hacer click
        botonforgot.setOnClickListener {
            val intent = Intent(this, Forgotpassword::class.java)
            startActivity(intent)

       }
        botonentrar.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = etPasswor.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                // Mostrar mensaje si alguno de los campos está vacío
                Toast.makeText(this, "Por favor ingrese su correo y contraseña", Toast.LENGTH_SHORT).show()
            }
        }
        //Accion del boton de visisvilidad
        botonvis.setOnClickListener {
            togglePasswordVisibility()
        }
    }


    private fun IniciarVariables() {

        botonvis = findViewById<ImageButton>(com.dgstifosi.myapplication.R.id.visibilityToggleButton2)
        botonforgot = findViewById<Button>(com.dgstifosi.myapplication.R.id.Btfp)
        editTextEmail = findViewById<EditText>(com.dgstifosi.myapplication.R.id.txtEmail)
        botonregistro = findViewById<Button>(com.dgstifosi.myapplication.R.id.tv_go_to_register)
        botonentrar= findViewById(com.dgstifosi.myapplication.R.id.buttonentrada)
        etPasswor = findViewById<EditText>(com.dgstifosi.myapplication.R.id.txtPassword)

    }

    //Logica para alternar la visivilidad
    private fun togglePasswordVisibility() {
        if (etPasswor.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            // Si la contraseña esta oculta la mostramos
            etPasswor.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL

            botonvis.setImageResource(android.R.drawable.ic_menu_view) // Cambia el icono a 'mostrar'
        } else {
            // Si la contraseñas está visible la ocultamos
            etPasswor.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            botonvis.setImageResource(android.R.drawable.ic_menu_close_clear_cancel) // Cambia el icono a 'ocultar'
        }

        // Para actualizar el cursor y la selección del EditText
        etPasswor.setSelection(etPasswor.text.length)

    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Inicio de sesión exitoso
                    val user = auth.currentUser
                    // Redirigir al usuario a la actividad principal
                    val intent = Intent(this, Hall::class.java)
                    startActivity(intent)
                    finish() // Termina la actividad de login para que no pueda volver atrás
                } else {
                    // Si el inicio de sesión falla, muestra un mensaje de error
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}


