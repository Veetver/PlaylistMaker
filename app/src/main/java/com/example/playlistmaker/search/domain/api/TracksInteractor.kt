package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.TrackList
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(query: SearchTrackQuery): Flow<Pair<TrackList?, Int?>>
}