package com.example.playlistmaker.library.di

import com.example.playlistmaker.library.presentation.viewmodel.FavoritesViewModel
import com.example.playlistmaker.library.presentation.viewmodel.PlaylistsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val libraryPresentationModule = module {
    viewModel {
        FavoritesViewModel()
    }
    viewModel {
        PlaylistsViewModel()
    }
}