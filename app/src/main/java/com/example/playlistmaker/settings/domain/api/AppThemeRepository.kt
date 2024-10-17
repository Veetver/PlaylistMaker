package com.example.playlistmaker.settings.domain.api

import com.example.playlistmaker.settings.domain.model.AppThemeMode

interface AppThemeRepository {
    fun getSavedTheme(): AppThemeMode
    fun changeTheme(mode: AppThemeMode): Boolean
}