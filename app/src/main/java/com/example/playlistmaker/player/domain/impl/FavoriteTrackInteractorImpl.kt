package com.example.playlistmaker.player.domain.impl

import android.util.Log
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
    override suspend fun getTrackListSortedByFavorite(currentList: TrackList): Flow<TrackList> = flow {
        val favoriteTrackIds = repository.getFavoriteTrackIds().toList()
        val sorted = currentList.list.map { track ->
            Log.d("PLM", "getTrackListSortedByFavorite: $track")
            // TODO: Почему при открытии плеера, трек не отображается как понравившийся?
            track.apply {
                isFavorite = favoriteTrackIds.contains(track.trackId)
            }
        }.sortedBy { it.isFavorite }

        emit(TrackList(sorted))
    }
}