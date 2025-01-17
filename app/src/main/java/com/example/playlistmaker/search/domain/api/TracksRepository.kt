package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(search: SearchTrackQuery): Flow<Resource<List<Track>>>
}