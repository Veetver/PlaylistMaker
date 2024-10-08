package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.model.AppThemeMode

class GetAppThemeModeUseCase(
    private val settingsRepository: SettingsRepository
) {
    fun execute(): AppThemeMode = settingsRepository.getSavedTheme()
}