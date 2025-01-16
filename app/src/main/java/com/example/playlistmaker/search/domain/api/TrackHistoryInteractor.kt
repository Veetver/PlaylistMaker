package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList

interface TrackHistoryInteractor {
    suspend fun addTrack(track: Track)
    suspend fun getHistory(): TrackList
    suspend fun clearHistory()
}