package com.dgstifosi.myapplication

import android.content.Intent
import android.net.Uri
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class Registro : AppCompatActivity() {

    private lateinit var etname:EditText
    private lateinit var editTextEmail:EditText
    private lateinit var etPasswordNu :EditText
    private lateinit var etPasswordNuR:EditText
    private lateinit var botonregistro :Button
    private lateinit var botonvis: ImageButton

    private lateinit var auth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)

        IniciarVariables()

        botonregistro.setOnClickListener{
        Validardatos()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val botonentrada =findViewById<Button>(com.dgstifosi.myapplication.R.id.backLogin)
        botonentrada.setOnClickListener{
            //Creamos un Intent para volver al inicio
            val intent = Intent(this, Entrada::class.java)
            startActivity(intent)
            finish()
        }
        //Accion del boton de visisvilidad
        botonvis.setOnClickListener {
            togglePasswordVisibility()
        }
    }

    //Logica para alternar la visivilidad
    private fun togglePasswordVisibility() {
        if (etPasswordNu.inputType == InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            // Si ambas contraseñas están ocultas, las mostramos
            etPasswordNu.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            etPasswordNuR.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            botonvis.setImageResource(android.R.drawable.ic_menu_view) // Cambia el icono a 'mostrar'
        } else {
            // Si ambas contraseñas están visibles, las ocultamos
            etPasswordNu.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            etPasswordNuR.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            botonvis.setImageResource(android.R.drawable.ic_menu_close_clear_cancel) // Cambia el icono a 'ocultar'
        }

        // Para actualizar el cursor y la selección de ambos EditText
        etPasswordNu.setSelection(etPasswordNu.text.length)
        etPasswordNuR.setSelection(etPasswordNuR.text.length)
    }

    private fun IniciarVariables (){

        botonvis = findViewById<ImageButton>(com.dgstifosi.myapplication.R.id.visibilityToggleButton)
        etname = findViewById<EditText>(com.dgstifosi.myapplication.R.id.txtFullName)
        editTextEmail = findViewById<EditText>(com.dgstifosi.myapplication.R.id.TxtEmailAddress)
        botonregistro = findViewById<Button>(com.dgstifosi.myapplication.R.id.registro)
        etPasswordNu = findViewById<EditText>(com.dgstifosi.myapplication.R.id.txtPasswordR)
        etPasswordNuR = findViewById<EditText>(com.dgstifosi.myapplication.R.id.txtPasswordRR)
        auth= FirebaseAuth.getInstance()//Iniciamos Firebase Auth
        firestore = FirebaseFirestore.getInstance()  // Iniciamos Firestore
        storage = FirebaseStorage.getInstance()  // Iniciamos Storage
    }



    private fun Validardatos() {
        val correo:String = editTextEmail.text.toString().trim()
        val contraseñaR:String = etPasswordNu.text.toString().trim()
        val nombre:String= etname.text.toString().trim()
        val contraseña:String = etPasswordNu.text.toString().trim()

        if (correo.isEmpty()){
            Toast.makeText(this, "Por favor, escribe el correo", Toast.LENGTH_SHORT).show()
        }
        else if (nombre.isEmpty()){
            Toast.makeText(this, "Por favor, escribe el Nombre", Toast.LENGTH_SHORT).show()
        }
        else if (contraseña.isEmpty()){
            Toast.makeText(this, "Por favor, escribe una contraseña", Toast.LENGTH_SHORT).show()
        }
        else if (contraseñaR.isEmpty()){
            Toast.makeText(this, "Por favor, repite una contraseña", Toast.LENGTH_SHORT).show()
        }
        else if (!contraseñaR.equals(contraseña)){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
        }
        else {
                Registrarusuario(correo,contraseña, nombre)
        }
    }

    private fun Registrarusuario(correo: String, contraseña: String, nombre: String) {
        auth.createUserWithEmailAndPassword(correo, contraseña)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    // Crear un documento en Firestore con los datos del usuario
                    val usuarioData = hashMapOf(
                        "nombre" to nombre,
                        "correo" to correo,
                        "fotoPerfil" to "url_de_foto_aqui",  // Suponiendo que se obtiene una URL
                        "uid" to user?.uid
                    )

                    // Guardar los datos en Firestore
                    firestore.collection("usuarios")
                        .document(user?.uid ?: "")
                        .set(usuarioData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show()
                            // Redirigir a la pantalla principal o login
                            val intent = Intent(this, Entrada::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al guardar los datos: $e", Toast.LENGTH_SHORT).show()
                        }

                    // Si deseas subir una imagen de perfil
                    subirFotoPerfil(user?.uid)
                } else {
                    Toast.makeText(this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun subirFotoPerfil(uid: String?) {
        val filePath: Uri = Uri.parse("ruta_de_imagen_aqui")  // Aquí pones la URI de la imagen (puede ser un ImageView)
        val storageRef = storage.reference.child("profile_pictures/$uid.jpg")

        storageRef.putFile(filePath)
            .addOnSuccessListener { taskSnapshot ->
                // Obtener la URL de la foto subida
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    // Actualiza Firestore con la URL de la foto de perfil
                    firestore.collection("usuarios")
                        .document(uid ?: "")
                        .update("fotoPerfil", uri.toString())
                        .addOnSuccessListener {
                            Toast.makeText(this, "Foto de perfil subida correctamente", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Error al actualizar la foto de perfil: $e", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al subir la foto de perfil: $e", Toast.LENGTH_SHORT).show()
            }
    }
}