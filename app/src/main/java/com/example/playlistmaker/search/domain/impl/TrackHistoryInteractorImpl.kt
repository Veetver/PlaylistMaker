package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.core.data.mappers.TrackDtoMapper.toTrack
import com.example.playlistmaker.core.data.mappers.TrackMapper.toTrackDto
import com.example.playlistmaker.search.data.LocalDataSource
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackHistoryInteractorImpl(
    private val local: LocalDataSource
) : TrackHistoryInteractor {
    override suspend fun addTrack(track: Track) = local.save(track.toTrackDto())
    override fun getHistory(): Flow<List<Track>> =
        local.getHistory().map { list -> list.map { it.toTrack() } }

    override suspend fun clearHistory(): Unit = local.clearHistory()
}