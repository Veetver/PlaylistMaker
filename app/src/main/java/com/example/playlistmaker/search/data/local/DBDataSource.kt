package com.example.playlistmaker.search.data.local

import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.mappers.TrackDtoMapper.toTrackHistoryEntity
import com.example.playlistmaker.core.data.mappers.TrackHistoryEntityMapper.toTrackDto
import com.example.playlistmaker.search.data.LocalDataSource
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.dto.TrackListDto

class DBDataSource(
    private val appDatabase: AppDatabase
): LocalDataSource {
    override suspend fun getHistory(): TrackListDto =
        TrackListDto(
            appDatabase
                .trackHistoryDao()
                .getTracks()
                .sortedByDescending { it.id }
                .map { it.toTrackDto() }
        )


    override suspend fun save(track: TrackDto) {
        appDatabase.trackHistoryDao().insert(track.toTrackHistoryEntity())
        appDatabase.trackHistoryDao().limitTable()
    }

    override suspend fun clearHistory() =appDatabase.trackHistoryDao().dropTable()
}