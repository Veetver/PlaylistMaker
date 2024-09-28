package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.LocalDataSource
import com.example.playlistmaker.data.RemoteDataSource
import com.example.playlistmaker.data.SettingsRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.local.SharedPrefsDataSource
import com.example.playlistmaker.data.network.RetrofitDataSource
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getRemoteDataSource(): RemoteDataSource {
        return RetrofitDataSource()
    }

    private fun getLocalDataSource(context: Context): LocalDataSource {
        return SharedPrefsDataSource(context)
    }

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(
            getRemoteDataSource(),
            getLocalDataSource(context),
        )
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }
}