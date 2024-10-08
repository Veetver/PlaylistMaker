package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackListDto

interface LocalDataSource {
    fun getSearchHistory(): TrackListDto
    fun saveSearchHistory(history: TrackListDto): Boolean
    fun clearSearchHistory(): Boolean
}