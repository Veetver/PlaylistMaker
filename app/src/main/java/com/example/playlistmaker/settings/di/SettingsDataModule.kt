package com.example.playlistmaker.settings.di

import com.example.playlistmaker.settings.data.AppThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.api.AppThemeRepository
import org.koin.dsl.module

val settingsDataModule = module {
    single<AppThemeRepository> {
        AppThemeRepositoryImpl(context = get())
    }
}