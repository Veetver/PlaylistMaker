package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface TrackHistoryInteractor {
    suspend fun addTrack(track: Track)
    fun getHistory(): Flow<List<Track>>
    suspend fun clearHistory()
}