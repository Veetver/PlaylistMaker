package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val searchPresentationModule = module {
    viewModel<SearchViewModel> {
        SearchViewModel(
            tracksInteractor = get(),
            tracksHistoryInteractor = get(),
            favoriteTrackInteractor = get(),
        )
    }
}