package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.presentation.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsPresentationModule = module {
    viewModel<SettingsViewModel> {
        SettingsViewModel(
            getAppThemeModeUseCase = get(),
            setAppThemeModelUseCase = get(),
            contactSupportUseCase = get(),
            openEulaUseCase = get(),
            shareAppUseCase = get(),
        )
    }
}