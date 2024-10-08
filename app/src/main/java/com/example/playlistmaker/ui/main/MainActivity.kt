package com.example.playlistmaker.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.ui.library.LibraryActivity
import com.example.playlistmaker.ui.search.SearchActivity
import com.example.playlistmaker.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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