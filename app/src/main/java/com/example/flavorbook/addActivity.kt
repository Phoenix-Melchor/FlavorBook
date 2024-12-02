package com.example.flavorbook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class addActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private lateinit var imagePickerLauncher: ActivityResultLauncher<String>

    private lateinit var back_btn: ImageView
    private lateinit var create_recipe: Button
    private lateinit var imagen: TextInputLayout
    private lateinit var nombre: TextInputLayout
    private lateinit var tipo_receta: TextInputLayout
    private lateinit var ingredientes: TextInputLayout
    private lateinit var instrucciones: TextInputLayout
    private lateinit var notas: TextInputLayout
    private lateinit var upload_photo: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add)

        back_btn = findViewById(R.id.back_btn)
        create_recipe = findViewById(R.id.create_receta)
        upload_photo = findViewById(R.id.upload_photo)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                Toast.makeText(this, "$uri", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se seleccionó ninguna imagen", Toast.LENGTH_SHORT).show()
            }
        }


        upload_photo.setOnClickListener {
            openImageSelect()
        }

        back_btn.setOnClickListener {
            finish()
        }

        create_recipe.setOnClickListener {
            createRecipe()
            finish()
        }
    }

    //Terminar para los permisos
    private fun openImageSelect() {
        imagePickerLauncher.launch("image/*")
    }

    private fun createRecipe(){
        imagen = findViewById(R.id.image_txt)
        nombre = findViewById(R.id.name)
        tipo_receta = findViewById(R.id.recipe_type)
        ingredientes = findViewById(R.id.ingrediente)
        instrucciones = findViewById(R.id.instrucciones)
        notas = findViewById(R.id.notas)

        //Agregar luego que se compruebe que ponga todos es adicional

        val storageRef = storage.reference
        val imageRef = storageRef.child("imagenes/${System.currentTimeMillis()}.jpg")
        val imageUri = Uri.fromFile(File("ruta_a_la_imagen"))
        val uploadTask = imageRef.putFile(imageUri)
        uploadTask.addOnSuccessListener {
            // Obtener la URL de descarga
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                val imageUrl = uri.toString()
                // Guarda esta URL en Firestore
                saveImageUrlToFirestore(imageUrl)
            }
        }.addOnFailureListener {
            println("Error al subir la imagen: ${it.message}")
        }

        val recipeData = hashMapOf(
            "Imagen" to imagen.editText?.text.toString().trim(),
            "NombreReceta" to nombre.editText?.text.toString().trim(),
            "TipoReceta" to tipo_receta.editText?.text.toString().trim(),
            "Ingredientes" to ingredientes.editText?.text.toString().trim(),
            "Instrucciones" to instrucciones.editText?.text.toString().trim(),
            "Notas" to notas.editText?.text.toString().trim()
        )

        db.collection("Recetas").add(recipeData).addOnSuccessListener { documentReference ->
            Toast.makeText(this, "Receta creada con éxito. ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
        }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al guardar la receta: $e", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveImageUrlToFirestore(imageUrl: String) {
        val recipeData = hashMapOf(
            "Imagen" to imageUrl,
            "NombreReceta" to "Salsa de Tomate",
            "Ingredientes" to "Tomates, Ajo, Cebolla",
            // Otros datos...
        )

        db.collection("Recetas")
            .add(recipeData)
            .addOnSuccessListener {
                println("Receta añadida con imagen: $imageUrl")
            }
            .addOnFailureListener {
                println("Error al guardar receta: ${it.message}")
            }
    }

}