package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.player.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class FavoriteTrackInteractorImpl(
    private val repository: FavoriteTrackRepository
) : FavoriteTrackInteractor {
    override suspend fun addFavoriteTrack(track: Track) = repository.addFavoriteTrack(track)
    override suspend fun removeFavoriteTrack(track: Track) = repository.removeFavoriteTrack(track)
    override suspend fun getTrackList(): Flow<TrackList> = repository.getFavoriteTracks()
    override suspend fun isFavorite(track: Track): Flow<Track> = flow {
        emit(track.apply {
            isFavorite = repository.getFavoriteTrackIds().toList().contains(track.trackId)
        })
    }
}