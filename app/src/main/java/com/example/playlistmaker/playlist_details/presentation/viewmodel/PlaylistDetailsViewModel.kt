package com.example.playlistmaker.playlist_details.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.mappers.PlaylistTrackEntityMapper.toTrack
import com.example.playlistmaker.core.domain.impl.GetPluralStringUseCase
import com.example.playlistmaker.core.domain.impl.GetStringFromIdUseCase
import com.example.playlistmaker.library_new_playlist.domain.impl.LoadFileUseCase
import com.example.playlistmaker.playlist_details.domain.api.SharePlaylistUseCase
import com.example.playlistmaker.playlist_details.presentation.state.PlaylistDetails
import com.example.playlistmaker.playlist_details.presentation.state.PlaylistDetailsState
import com.example.playlistmaker.playlist_details.presentation.state.SnackbarState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistDetailsViewModel(
    private val playlistId: Long,
    private val appDatabase: AppDatabase,
    private val sharePlaylistUseCase: SharePlaylistUseCase,
    private val getStringFromIdUseCase: GetStringFromIdUseCase,
    private val getPluralStringFromIdUseCase: GetPluralStringUseCase,
    private val loadFileUseCase: LoadFileUseCase,
) : ViewModel() {

    private val _playlistsDetailsScreenState: MutableStateFlow<PlaylistDetailsState> =
        MutableStateFlow(PlaylistDetailsState(isLoading = true))
    val playlistsDetailsScreenState = _playlistsDetailsScreenState.asStateFlow()

    private val _isNeedShowSnackbar: MutableStateFlow<SnackbarState> =
        MutableStateFlow(SnackbarState())
    val isNeedShowSnackbar = _isNeedShowSnackbar.asStateFlow()

    fun loadPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            _playlistsDetailsScreenState.update { it.copy(isLoading = true) }
            appDatabase
                .playlistDao()
                .getPlaylist(playlistId)
                .firstOrNull()?.let { playlist ->
                    _playlistsDetailsScreenState.update {
                        it.copy(
                            playlist = PlaylistDetails(
                                name = playlist.name,
                                cover = loadFileUseCase(playlist.coverName.toString()).firstOrNull(),
                                description = playlist.description,
                            )
                        )
                    }
                }

            appDatabase
                .playlistDao()
                .getPlaylistTracks(playlistId)
                .collect { playlistTracks ->
                    _playlistsDetailsScreenState.update { state ->
                        state.copy(
                            isLoading = false, playlist = state.playlist?.copy(
                                trackCount = playlistTracks.size,
                                trackList = playlistTracks.map { it.toTrack() })
                        )
                    }
                }
        }
    }

    fun removeTrackFromPlaylist(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.playlistDao().removeTrackFromPlaylistAndClear(playlistId, track.trackId)
        }
    }

    fun sharePlaylist() {
        val playlist = playlistsDetailsScreenState.value.playlist
        if (playlist != null && playlist.trackCount > 0) {
            val builder = StringBuilder()
            builder.append(playlist.name + "\n")
            if (!playlist.description.isNullOrEmpty()) {
                builder.append(playlist.description + "\n")
            }
            builder.append(getPluralStringFromIdUseCase(R.plurals.tracks, playlist.trackCount))
            playlistsDetailsScreenState.value.playlist?.trackList?.forEachIndexed { index, track ->
                val trackTime =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)
                builder.append("\n")
                builder.append("${index + 1}. ${track.artistName} - ${track.trackName} (${trackTime})")
            }
            sharePlaylistUseCase(builder.toString())
        } else {
            showSnackbar(getStringFromIdUseCase(R.string.share_no_tracks))
        }
    }

    private fun showSnackbar(text: String) {
        _isNeedShowSnackbar.update { it.copy(isNeedShow = true, text = text) }
    }

    fun clearSnackbar() {
        _isNeedShowSnackbar.update { it.copy(isNeedShow = false) }
    }

    fun removePlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase.playlistDao().removePlaylistAndClear(playlistId)
        }
    }
}