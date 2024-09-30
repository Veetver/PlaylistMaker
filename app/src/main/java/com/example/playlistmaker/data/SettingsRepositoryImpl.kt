package com.example.playlistmaker.data

import android.app.Application.MODE_PRIVATE
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.model.AppThemeMode

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    private val prefs = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)

    override fun getSavedTheme(): AppThemeMode {
        return if (prefs.getBoolean(SHARED_PREFS_DARK_THEME, false)) {
            AppThemeMode.DarkMode
        } else {
            AppThemeMode.LightMode
        }
    }

    override fun changeTheme(mode: AppThemeMode): Boolean {
        when (mode) {
            AppThemeMode.DarkMode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }

            AppThemeMode.LightMode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return saveTheme(mode)
    }

    private fun saveTheme(mode: AppThemeMode): Boolean {
        prefs.edit().putBoolean(SHARED_PREFS_DARK_THEME, mode.value).apply()
        return true
    }

    companion object {
        const val SHARED_PREFS = "com.example.playlistmaker"
        private const val SHARED_PREFS_DARK_THEME = "dark_theme"
    }
}