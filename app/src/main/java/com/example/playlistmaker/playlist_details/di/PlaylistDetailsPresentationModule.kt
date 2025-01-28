package com.example.playlistmaker.playlist_details.di

import com.example.playlistmaker.library.presentation.model.CreatedPlaylist
import com.example.playlistmaker.playlist_details.presentation.viewmodel.PlaylistDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playlistDetailsPresentationModule = module {
    viewModel { (playlist: CreatedPlaylist) ->
        PlaylistDetailsViewModel(
            playlist = playlist,
            appDatabase = get(),
        )
    }
}