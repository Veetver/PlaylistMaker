package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList

interface TrackHistoryInteractor {
    fun addTrack(track: Track)
    fun getHistory(): TrackList
    fun clearHistory()
}