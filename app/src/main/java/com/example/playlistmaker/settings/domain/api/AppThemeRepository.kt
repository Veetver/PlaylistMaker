package com.example.playlistmaker.settings.domain.api

import com.example.playlistmaker.domain.model.AppThemeMode

interface AppThemeRepository {
    fun getSavedTheme(): AppThemeMode
    fun changeTheme(mode: AppThemeMode): Boolean
}