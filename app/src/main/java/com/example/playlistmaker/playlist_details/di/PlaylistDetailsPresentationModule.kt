package com.example.playlistmaker.playlist_details.di

import com.example.playlistmaker.playlist_details.presentation.viewmodel.PlaylistDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistDetailsPresentationModule = module {
    viewModel { (playlistId: Long) ->
        PlaylistDetailsViewModel(
            playlistId = playlistId,
            appDatabase = get(),
            sharePlaylistUseCase = get(),
            getPluralStringFromIdUseCase = get(),
            loadFileUseCase = get(),
            getStringFromIdUseCase = get(),
        )
    }
}