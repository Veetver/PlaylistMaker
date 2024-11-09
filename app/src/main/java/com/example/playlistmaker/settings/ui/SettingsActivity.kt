package com.example.playlistmaker.settings.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT, Color.TRANSPARENT
            ) { true },
        )
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.appThemeModeLiveData.observe(this) { mode ->
            binding.themeSwitcher.setOnCheckedChangeListener(null)
            binding.themeSwitcher.isChecked = mode.value
            binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
                viewModel.changeTheme(checked)
            }
        }

        binding.toolbarSettings.setNavigationOnClickListener { this.finish() }

        binding.shareAppItemSettings.setOnClickListener { viewModel.shareApp() }

        binding.supportItemSettings.setOnClickListener { viewModel.contactSupport() }

        binding.eulaItemSettings.setOnClickListener { viewModel.openEula() }
    }
}