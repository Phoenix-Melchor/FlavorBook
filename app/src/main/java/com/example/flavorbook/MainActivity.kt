package com.example.flavorbook

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var details_btn: ImageView
    private lateinit var add_btn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        details_btn = findViewById(R.id.details_btn)
        add_btn = findViewById(R.id.add_btn)

        details_btn.setOnClickListener {
            intent = Intent(this, detailsActivity::class.java)
            startActivity(intent)
        }

        add_btn.setOnClickListener{
            intent = Intent(this, addActivity::class.java)
            startActivity(intent)
        }
    }
}