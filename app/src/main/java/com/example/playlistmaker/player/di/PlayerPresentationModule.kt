package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerPresentationModule = module {
    viewModel { (jsonTrack: String) ->
        PlayerViewModel(
            jsonTrack = jsonTrack,
            gson = get(),
            playerInteractor = get(),
        )
    }
}