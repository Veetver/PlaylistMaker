package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.domain.usecase.GetAppThemeModeUseCase
import com.example.playlistmaker.settings.domain.usecase.SetAppThemeModeUseCase
import org.koin.dsl.module

val settingsDomainModule = module {
    factory {
        GetAppThemeModeUseCase(appThemeRepository = get())
    }

    factory {
        SetAppThemeModeUseCase(appThemeRepository = get())
    }
}