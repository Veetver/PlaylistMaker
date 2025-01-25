package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerPresentationModule = module {
    viewModel { (track: Track) ->
        PlayerViewModel(
            track = track,
            playerInteractor = get(),
            favoriteTrackInteractor = get(),
            appDatabase = get(),
            loadFileUseCase = get(),
        )
    }
}