package com.example.playlistmaker.library.presentation.state

import com.example.playlistmaker.search.domain.model.TrackList

sealed class FavoritesScreenState {
    data object Loading: FavoritesScreenState()
    data class Content(val trackList: TrackList): FavoritesScreenState()
    data object Empty: FavoritesScreenState()
}