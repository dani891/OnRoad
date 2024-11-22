package com.dgstifosi.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class Entrada : AppCompatActivity() {

    private lateinit var botonvis: ImageButton
    private lateinit var etPasswor:EditText
    private lateinit var editTextEmail:EditText
    private lateinit var botonregistro:Button
    private lateinit var botonforgot:Button
    private lateinit var botonentrar:Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_entrada)

        IniciarVariables()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
        // Acción de el boton inicio al hacer click
        botonentrar.setOnClickListener {
            val intent = Intent(this,Hall::class.java)
            startActivity(intent)
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

}


