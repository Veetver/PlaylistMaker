package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.SearchResult
import com.example.playlistmaker.domain.model.SearchTrackQuery
import com.example.playlistmaker.domain.model.TrackList

interface TracksRepository {
    fun searchTracks(search: SearchTrackQuery): SearchResult
    fun getSearchHistory(): TrackList
    fun saveSearchHistory(history: TrackList): Boolean
    fun clearSearchHistory(): Boolean
}