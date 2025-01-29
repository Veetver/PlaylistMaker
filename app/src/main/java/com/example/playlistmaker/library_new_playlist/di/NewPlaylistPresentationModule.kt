package com.example.playlistmaker.library_new_playlist.di

import com.example.playlistmaker.library_new_playlist.presentation.viewmodel.NewPlaylistViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newPlaylistPresentationModule = module {
    viewModel {
        NewPlaylistViewModel(
            saveFileUseCase = get(),
            appDatabase = get(),
            loadFileUseCase = get(),
        )
    }
}