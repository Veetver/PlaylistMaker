package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.player.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class FavoriteTrackInteractorImpl(
    private val repository: FavoriteTrackRepository
) : FavoriteTrackInteractor {
    override suspend fun addFavoriteTrack(track: Track) = repository.addFavoriteTrack(track)
    override suspend fun removeFavoriteTrack(track: Track) = repository.removeFavoriteTrack(track)
    override suspend fun getTracks(): Flow<List<Track>> = repository.getFavoriteTracks()
    override suspend fun isFavorite(track: Track): Flow<Track> = flow {
        repository
            .getFavoriteTrackIds()
            .first()
            .let {
                if (it.contains(track.trackId)) {
                    track.apply { isFavorite = true }
                }
            }
        emit(track)
    }
}