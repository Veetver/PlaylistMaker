package com.example.playlistmaker.settings.domain.usecase

import com.example.playlistmaker.settings.domain.api.AppThemeRepository
import com.example.playlistmaker.settings.domain.model.AppThemeMode

class GetAppThemeModeUseCase(
    private val appThemeRepository: AppThemeRepository
) {
    fun execute(): AppThemeMode = appThemeRepository.getSavedTheme()
}