package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.domain.model.SearchTrackQuery

interface TracksRepository {
    fun searchTracks(search: SearchTrackQuery): SearchResult
}