package com.example.playlistmaker.player.data

import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.mappers.TrackEntityMapper.toTrack
import com.example.playlistmaker.core.data.mappers.TrackMapper.toTrackEntity
import com.example.playlistmaker.player.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
) : FavoriteTrackRepository {
    override suspend fun addFavoriteTrack(track: Track) {
        appDatabase.trackDao().insertTrack(track = track.toTrackEntity())
    }

    override suspend fun removeFavoriteTrack(track: Track) {
        appDatabase.trackDao().deleteTrack(track = track.toTrackEntity())
    }

    override suspend fun getFavoriteTracks(): Flow<TrackList> = flow {
        val tracks = appDatabase.trackDao().getTracks()
        emit(TrackList(tracks.map { it.toTrack() }))
    }

    override suspend fun getFavoriteTrackIds(): Flow<Long> = appDatabase.trackDao().getTrackIds().asFlow()
}