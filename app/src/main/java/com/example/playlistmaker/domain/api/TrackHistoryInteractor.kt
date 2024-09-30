package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackList

interface TrackHistoryInteractor {
    fun addTrack(track: Track)
    fun getHistory(): TrackList
    fun clearHistory()
}