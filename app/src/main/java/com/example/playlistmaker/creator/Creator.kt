package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.LocalDataSource
import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.data.RemoteDataSource
import com.example.playlistmaker.data.SettingsRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.local.SharedPrefsDataSource
import com.example.playlistmaker.data.network.RetrofitDataSource
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.usecase.GetAppThemeModeUseCase
import com.example.playlistmaker.domain.usecase.SetAppThemeModeUseCase
import com.example.playlistmaker.presentation.App
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

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(getApplicationContext())
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

    fun provideGson(): Gson {
        return gson
    }
}