package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getHistory(): Flow<List<TrackDto>>
    suspend fun save(track: TrackDto)
    suspend fun clearHistory()
}