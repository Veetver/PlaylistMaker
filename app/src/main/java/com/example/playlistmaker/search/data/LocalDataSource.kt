package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.dto.TrackListDto

interface LocalDataSource {
    suspend fun getHistory(): TrackListDto
    suspend fun save(track: TrackDto)
    suspend fun clearHistory()
}