package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.TrackList
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(search: SearchTrackQuery): Flow<Resource<TrackList>>
}