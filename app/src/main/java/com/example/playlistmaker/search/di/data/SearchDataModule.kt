package com.example.playlistmaker.search.di.data

import com.example.playlistmaker.search.data.LocalDataSource
import com.example.playlistmaker.search.data.RemoteDataSource
import com.example.playlistmaker.search.data.TracksRepositoryImpl
import com.example.playlistmaker.search.data.local.DBDataSource
import com.example.playlistmaker.search.data.network.RetrofitDataSource
import com.example.playlistmaker.search.domain.api.TracksRepository
import org.koin.dsl.module

val searchDataModule = module {
    includes(searchNetworkModule)

    single<LocalDataSource> {
        DBDataSource(
            appDatabase = get(),
        )
    }

    single<RemoteDataSource> {
        RetrofitDataSource(
            apiService = get(),
            get(),
        )
    }

    single<TracksRepository> {
        TracksRepositoryImpl(remoteDataSource = get())
    }
}