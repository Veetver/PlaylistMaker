package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import org.koin.dsl.module

val playerDomainModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(playerRepository = get())
    }

    factory<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(repository = get())
    }
}