package com.example.playlistmaker.presentation

import android.app.Application
import android.content.Context
import com.example.playlistmaker.creator.Creator

class App : Application() {

    init {
        instance = this
    }

    private val setAppThemeModeUseCase by lazy { Creator.provideSetAppThemeModeUseCase() }
    private val getAppThemeModeUseCase by lazy { Creator.provideGetAppThemeModeUseCase() }

    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext

        setAppThemeModeUseCase.execute(getAppThemeModeUseCase.execute())
    }

    companion object {
        lateinit var instance: Application
            private set
    }
}