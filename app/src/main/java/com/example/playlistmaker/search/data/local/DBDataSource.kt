package com.example.playlistmaker.search.data.local

import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.mappers.TrackDtoMapper.toTrackHistoryEntity
import com.example.playlistmaker.core.data.mappers.TrackHistoryEntityMapper.toTrackDto
import com.example.playlistmaker.search.data.LocalDataSource
import com.example.playlistmaker.search.data.dto.TrackDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DBDataSource(
    private val appDatabase: AppDatabase
) : LocalDataSource {
    override fun getHistory(): Flow<List<TrackDto>> =
        appDatabase
            .trackHistoryDao()
            .getTracks()
            .map { list ->
                list.map { it.toTrackDto() }
            }

    override suspend fun save(track: TrackDto) {
        appDatabase
            .trackHistoryDao()
            .insert(track.toTrackHistoryEntity())

        appDatabase
            .trackHistoryDao()
            .limitTable()
    }

    override suspend fun clearHistory() =
        appDatabase
            .trackHistoryDao()
            .dropTable()
}