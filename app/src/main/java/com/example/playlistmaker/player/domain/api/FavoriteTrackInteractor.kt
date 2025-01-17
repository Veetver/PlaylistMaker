package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    suspend fun addFavoriteTrack(track: Track)
    suspend fun removeFavoriteTrack(track: Track)
    suspend fun getTracks(): Flow<List<Track>>
    suspend fun isFavorite(track: Track): Flow<Track>
}