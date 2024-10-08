package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.AppThemeMode

interface SettingsRepository {
    fun getSavedTheme(): AppThemeMode
    fun changeTheme(mode: AppThemeMode): Boolean
}