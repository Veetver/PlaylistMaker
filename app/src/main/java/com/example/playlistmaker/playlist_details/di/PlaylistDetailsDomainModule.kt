package com.example.playlistmaker.playlist_details.di

import com.example.playlistmaker.playlist_details.domain.api.SharePlaylistUseCase
import org.koin.dsl.module

val playlistDetailsDomainModule = module {
    factory {
        SharePlaylistUseCase(
            externalNavigator = get()
        )
    }
}