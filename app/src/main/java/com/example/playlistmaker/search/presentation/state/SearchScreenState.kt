package com.example.playlistmaker.search.presentation.state

import com.example.playlistmaker.search.domain.model.Track

sealed interface SearchScreenState {
    data object Loading : SearchScreenState
    data object Default : SearchScreenState
    data class SearchContent(val trackList: List<Track>) : SearchScreenState
    data class HistoryContent(val trackList: List<Track>) : SearchScreenState
    data object Error : SearchScreenState
    data object Empty : SearchScreenState
}