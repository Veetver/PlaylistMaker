package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val search = findViewById<Button>(R.id.btn_search)
        val library = findViewById<Button>(R.id.btn_library)
        val settings = findViewById<Button>(R.id.btn_settings)

        search.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        library.setOnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
        }

        settings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}