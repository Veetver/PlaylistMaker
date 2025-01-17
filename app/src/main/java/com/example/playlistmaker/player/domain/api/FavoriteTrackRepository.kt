package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {
    suspend fun addFavoriteTrack(track: Track)
    suspend fun removeFavoriteTrack(track: Track)
    suspend fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun getFavoriteTrackIds(): Flow<List<Long>>
}