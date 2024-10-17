package com.example.playlistmaker.search.presentation.state

import com.example.playlistmaker.search.domain.model.TrackList

sealed interface SearchScreenState {
    data object Loading : SearchScreenState
    data object Default : SearchScreenState
    data class SearchContent(val trackList: TrackList) : SearchScreenState
    data class HistoryContent(val trackList: TrackList) : SearchScreenState
    data object Error : SearchScreenState
    data object Empty : SearchScreenState
}