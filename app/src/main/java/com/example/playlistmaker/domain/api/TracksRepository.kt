package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.SearchResult
import com.example.playlistmaker.domain.model.SearchTrackQuery

interface TracksRepository {
    fun searchTracks(search: SearchTrackQuery): SearchResult
}