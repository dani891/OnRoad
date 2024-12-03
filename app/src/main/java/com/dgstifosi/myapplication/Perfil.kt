package com.dgstifosi.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Perfil : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private lateinit var imagenperfil: ImageView
    // Declaro el lanzador para obtener resultados
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        imagenperfil = findViewById(R.id.imgPerfil)
        val botonsalir:Button = findViewById<Button>(R.id.btnhall)
        val botoncambio:Button= findViewById<Button>(R.id.btncambio)
        val botonLogin :Button= findViewById<Button>(R.id.backLogin2)
        val botonviaje:Button= findViewById<Button>(R.id.Contenido)


        // Configurar el lanzador para obtener resultados
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val imageUri: Uri? = result.data?.data
                imagenperfil.setImageURI(imageUri) // Actualiza la imagen de perfil
            }
        }

        // Botón para cambiar la foto de perfil
        botoncambio.setOnClickListener {
            openGallery()
        }

        // Botón para volver al login
        botonLogin.setOnClickListener {
            val intent = Intent(this, Entrada::class.java)
            startActivity(intent)
            finish()
        }
        // Botón para volver al hall
        botonsalir.setOnClickListener {
            val intent = Intent(this, Hall::class.java)
            startActivity(intent)
            finish()
        }
        // Botón para contenido
        botonviaje.setOnClickListener {
            val intent = Intent(this, Cambiops::class.java)
            startActivity(intent)
        }

        // Verificar si el usuario está autenticado
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            val intent = Intent(this, Entrada::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    fun onChangeProfilePicClick() {
        openGallery()
    }
}