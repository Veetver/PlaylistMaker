package com.example.playlistmaker.settings.domain.usecase

import com.example.playlistmaker.settings.domain.api.AppThemeRepository
import com.example.playlistmaker.settings.domain.model.AppThemeMode

class SetAppThemeModeUseCase(
    private val appThemeRepository: AppThemeRepository
) {
    fun execute(mode: AppThemeMode): Boolean {
        return appThemeRepository.changeTheme(mode)
    }
}