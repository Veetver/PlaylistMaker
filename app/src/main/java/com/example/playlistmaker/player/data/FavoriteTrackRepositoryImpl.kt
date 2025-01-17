package com.example.playlistmaker.player.data

import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.mappers.TrackEntityMapper.toTrack
import com.example.playlistmaker.core.data.mappers.TrackMapper.toTrackEntity
import com.example.playlistmaker.player.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase,
) : FavoriteTrackRepository {
    override suspend fun addFavoriteTrack(track: Track) {
        appDatabase.trackFavoriteDao().insertTrack(track = track.toTrackEntity())
    }

    override suspend fun removeFavoriteTrack(track: Track) {
        appDatabase.trackFavoriteDao().deleteTrack(trackId = track.trackId)
    }

    override suspend fun getFavoriteTracks(): Flow<List<Track>> = appDatabase.trackFavoriteDao().getTracks().map { list -> list.map { it.toTrack() }}

    override suspend fun getFavoriteTrackIds(): Flow<List<Long>> = appDatabase.trackFavoriteDao().getTrackIds()
}