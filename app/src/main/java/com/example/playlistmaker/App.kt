package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.di.appModule
import com.example.playlistmaker.settings.domain.usecase.GetAppThemeModeUseCase
import com.example.playlistmaker.settings.domain.usecase.SetAppThemeModeUseCase
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    private val setAppThemeModeUseCase: SetAppThemeModeUseCase by inject()
    private val getAppThemeModeUseCase: GetAppThemeModeUseCase by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
        setAppThemeModeUseCase.execute(getAppThemeModeUseCase.execute())
    }
}