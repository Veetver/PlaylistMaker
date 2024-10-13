package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackListDto

interface LocalDataSource {
    fun getSearchHistory(): TrackListDto
    fun saveSearchHistory(history: TrackListDto): Boolean
    fun clearSearchHistory(): Boolean
}