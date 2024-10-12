package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.AppThemeRepositoryImpl
import com.example.playlistmaker.data.LocalDataSource
import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.data.RemoteDataSource
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.local.SharedPrefsDataSource
import com.example.playlistmaker.data.network.RetrofitDataSource
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.usecase.GetAppThemeModeUseCase
import com.example.playlistmaker.domain.usecase.SetAppThemeModeUseCase
import com.example.playlistmaker.presentation.App
import com.example.playlistmaker.settings.domain.api.AppThemeRepository
import com.example.playlistmaker.share.data.ExternalNavigator
import com.example.playlistmaker.share.data.ShareRepositoryImpl
import com.example.playlistmaker.share.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.share.domain.api.ShareRepository
import com.example.playlistmaker.share.domain.usecase.ContactSupportUseCase
import com.example.playlistmaker.share.domain.usecase.OpenEulaUseCase
import com.example.playlistmaker.share.domain.usecase.ShareAppUseCase
import com.google.gson.Gson

object Creator {
    private val gson by lazy { Gson() }

    private fun getApplicationContext(): Context {
        return App.instance.applicationContext
    }

    private fun getRemoteDataSource(): RemoteDataSource {
        return RetrofitDataSource()
    }

    private fun getLocalDataSource(): LocalDataSource {
        return SharedPrefsDataSource(getApplicationContext())
    }

    private fun getTracksRepository(): TracksRepository {
        return TracksRepositoryImpl(getRemoteDataSource())
    }

    fun provideTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getSettingsRepository(): AppThemeRepository {
        return AppThemeRepositoryImpl(getApplicationContext())
    }

    fun provideSetAppThemeModeUseCase(): SetAppThemeModeUseCase {
        return SetAppThemeModeUseCase(getSettingsRepository())
    }

    fun provideGetAppThemeModeUseCase(): GetAppThemeModeUseCase {
        return GetAppThemeModeUseCase(getSettingsRepository())
    }

    private fun getPlayerRepository(): PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun proidePlayerInterator(): PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }

    fun provideTrackHistoryInteractor(): TrackHistoryInteractor {
        return TrackHistoryInteractorImpl(getLocalDataSource())
    }

    private fun provideExternalNavigator(): ExternalNavigator {
        return ExternalNavigatorImpl(getApplicationContext())
    }

    private fun provideShareRepository(): ShareRepository {
        return ShareRepositoryImpl(
            context = getApplicationContext(),
            externalNavigator = provideExternalNavigator()
        )
    }

    fun provideShareAppUseCase(): ShareAppUseCase {
        return ShareAppUseCase(provideShareRepository())
    }

    fun provideContactSupportUseCase(): ContactSupportUseCase {
        return ContactSupportUseCase(provideShareRepository())
    }

    fun provideOpenEulaUseCase(): OpenEulaUseCase {
        return OpenEulaUseCase(provideShareRepository())
    }

    fun provideGson(): Gson {
        return gson
    }
}