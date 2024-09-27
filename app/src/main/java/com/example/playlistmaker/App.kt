package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.domain.usecase.GetAppThemeModeUseCase
import com.example.playlistmaker.domain.usecase.SetAppThemeModeUseCase

class App : Application() {

    private val settingsRepository by lazy { Creator.provideSettingsRepository(applicationContext) }
    private val setAppThemeModeUseCase by lazy { SetAppThemeModeUseCase(settingsRepository) }
    private val getAppThemeModeUseCase by lazy { GetAppThemeModeUseCase(settingsRepository) }

    override fun onCreate() {
        super.onCreate()

        setAppThemeModeUseCase.execute(getAppThemeModeUseCase.execute())
    }
}