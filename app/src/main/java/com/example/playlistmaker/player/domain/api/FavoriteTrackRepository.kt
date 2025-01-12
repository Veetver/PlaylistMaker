package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {
    suspend fun addFavoriteTrack(track: Track)
    suspend fun removeFavoriteTrack(track: Track)
    suspend fun getFavoriteTracks(): Flow<TrackList>
    suspend fun getFavoriteTrackIds(): Flow<Long>
}