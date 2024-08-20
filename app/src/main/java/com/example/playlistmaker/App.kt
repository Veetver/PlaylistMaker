package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    companion object {
        const val SHARED_PREFS = "com.example.playlistmaker"
        private const val SHARED_PREFS_DARK_THEME = "dark_theme"
    }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(SHARED_PREFS_DARK_THEME, false)
        switchTheme(darkTheme)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        saveTheme(darkThemeEnabled)
    }

    private fun saveTheme(darkThemeEnabled: Boolean) {
        getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
            .edit()
            .putBoolean(SHARED_PREFS_DARK_THEME, darkThemeEnabled)
            .apply()
    }

    fun getSharedPrefs(): SharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)
}