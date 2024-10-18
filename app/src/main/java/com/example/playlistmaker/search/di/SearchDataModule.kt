package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.LocalDataSource
import com.example.playlistmaker.search.data.RemoteDataSource
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.local.SharedPrefsDataSource
import com.example.playlistmaker.search.data.network.RetrofitDataSource
import com.example.playlistmaker.search.domain.api.TracksRepository
import org.koin.dsl.module

val searchDataModule = module {
    single<LocalDataSource> {
        SharedPrefsDataSource(
            context = get(),
        )
    }

    single<RemoteDataSource> {
        RetrofitDataSource()
    }

    single<TracksRepository> {
        TracksRepositoryImpl(remoteDataSource = get())
    }
}