package com.example.playlistmaker.domain.usecase

import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.model.AppThemeMode

class SetAppThemeModeUseCase(
    private val settingsRepository: SettingsRepository
) {
    fun execute(mode: AppThemeMode) {
        settingsRepository.changeTheme(mode)
    }
}