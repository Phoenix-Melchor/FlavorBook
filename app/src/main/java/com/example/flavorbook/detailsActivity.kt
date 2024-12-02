package com.example.flavorbook

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class detailsActivity : AppCompatActivity() {
    private lateinit var back_btn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_details)

        back_btn = findViewById(R.id.back_btn)

        back_btn.setOnClickListener {
            finish()
        }
    }
}