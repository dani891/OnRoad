package com.dgstifosi.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Cambiops : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var txtComentario: EditText
    private lateinit var btnImagen: Button
    private lateinit var btnSubir: Button
    private lateinit var botonEntrada: Button

    private var selectedImageUri: Uri? = null  // Variable para almacenar la URI de la imagen seleccionada

    private val storage: FirebaseStorage = FirebaseStorage.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cambiops)

        // Ajustes de la interfaz para Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar vistas
        imageView = findViewById(R.id.imageView5)
        txtComentario = findViewById(R.id.txtcomentario)  // Cambié el id a edtDescription para que coincida con el XML
        btnImagen = findViewById(R.id.btnImagen)
        btnSubir = findViewById(R.id.btnSubir)
        botonEntrada = findViewById(R.id.backLogin3)

        // Abrir selector de imágenes
        btnImagen.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            resultLauncher.launch(intent)
        }

        // Subir imagen y descripción a Firebase
        btnSubir.setOnClickListener {
            val description = txtComentario.text.toString()
            if (selectedImageUri != null && description.isNotBlank()) {
                uploadImageToFirebase(selectedImageUri!!, description)
            } else {
                Toast.makeText(this, "Por favor selecciona una imagen y escribe una descripción", Toast.LENGTH_SHORT).show()
            }
        }

        // Volver al login (u otra actividad)
        botonEntrada.setOnClickListener {
            val intent = Intent(this, Entrada::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Manejo del resultado de la selección de imagen
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data
                selectedImageUri = imageUri
                imageView.setImageURI(imageUri)  // Mostrar la imagen seleccionada
            }
        }

    // Subir la imagen seleccionada a Firebase Storage
    private fun uploadImageToFirebase(imageUri: Uri, description: String) {
        val userId = auth.currentUser?.uid ?: "unknown_user"
        val storageRef: StorageReference = storage.reference.child("images/$userId/${System.currentTimeMillis()}")

        val fileRef = storageRef.child("image.jpg")

        fileRef.putFile(imageUri)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                    // Obtener la URL de la imagen subida
                    val imageUrl = uri.toString()

                    // Subir la descripción junto con la URL de la imagen a Firestore
                    val imageData = hashMapOf(
                        "imageUrl" to imageUrl,
                        "description" to description,
                        "timestamp" to System.currentTimeMillis()
                    )

                    firestore.collection("images").add(imageData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Imagen y descripción subidas exitosamente!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Cambiops", "Error al subir la descripción: ", e)
                            Toast.makeText(this, "Error al subir la descripción", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Cambiops", "Error al subir la imagen: ", e)
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
            }
    }
}
