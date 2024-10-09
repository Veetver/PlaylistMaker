package com.example.playlistmaker.ui.library

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R

class LibraryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ) { true },
        )
        setContentView(R.layout.activity_library)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_library)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}