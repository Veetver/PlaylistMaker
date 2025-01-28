package com.example.playlistmaker.playlist_details.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.mappers.PlaylistTrackEntityMapper.toTrack
import com.example.playlistmaker.library.presentation.model.CreatedPlaylist
import com.example.playlistmaker.playlist_details.presentation.state.PlaylistDetails
import com.example.playlistmaker.playlist_details.presentation.state.PlaylistDetailsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistDetailsViewModel(
    private val playlist: CreatedPlaylist,
    private val appDatabase: AppDatabase,
) : ViewModel() {
    private val _playlistsDetailsScreenState: MutableStateFlow<PlaylistDetailsState> =
        MutableStateFlow(PlaylistDetailsState(isLoading = true))
    val playlistsDetailsScreenState = _playlistsDetailsScreenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val playlistTracks =
                appDatabase
                    .playlistDao()
                    .getPlaylistTracks(playlist.id)
                    .first()

            val playlistDetails = PlaylistDetails(
                name = playlist.name,
                trackCount = playlistTracks.size,
                cover = playlist.cover,
                description = playlist.description,
                trackList = playlistTracks.map { it.toTrack() }
            )


            _playlistsDetailsScreenState.update {
                it.copy(
                    isLoading = false,
                    playlist = playlistDetails
                )
            }
        }
    }
}