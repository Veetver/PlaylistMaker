package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.core.data.mappers.TrackMapper.toTrackDto
import com.example.playlistmaker.search.data.LocalDataSource
import com.example.playlistmaker.search.data.mapper.TrackListMapper.toTrackList
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList

class TrackHistoryInteractorImpl(
    private val local: LocalDataSource
) : TrackHistoryInteractor {
    override suspend fun addTrack(track: Track) = local.save(track.toTrackDto())
    override suspend fun getHistory(): TrackList = toTrackList(local.getHistory())
    override suspend fun clearHistory(): Unit = local.clearHistory()
}