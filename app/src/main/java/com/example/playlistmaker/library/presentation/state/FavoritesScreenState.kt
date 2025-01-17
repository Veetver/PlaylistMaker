package com.example.playlistmaker.library.presentation.state

import com.example.playlistmaker.search.domain.model.Track

sealed class FavoritesScreenState {
    data object Loading: FavoritesScreenState()
    data class Content(val trackList: List<Track>): FavoritesScreenState()
    data object Empty: FavoritesScreenState()
}