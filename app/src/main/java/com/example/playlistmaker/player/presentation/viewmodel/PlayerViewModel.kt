package com.example.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.db.entity.PlaylistAndTrackEntity
import com.example.playlistmaker.core.data.mappers.TrackMapper.toPlaylistTrackEntity
import com.example.playlistmaker.library.presentation.model.CreatedPlaylist
import com.example.playlistmaker.library.presentation.state.PlaylistsState
import com.example.playlistmaker.library_new_playlist.domain.impl.LoadFileUseCase
import com.example.playlistmaker.player.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.state.PlayerTrackState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.playlistmaker.player.presentation.mapper.TrackMapper as TrackMapperUI

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val appDatabase: AppDatabase,
    private val loadFileUseCase: LoadFileUseCase,
) : ViewModel() {

    private val _playerScreenState: MutableStateFlow<PlayerScreenState> =
        MutableStateFlow(PlayerScreenState.Initializing(TrackMapperUI.toTrackUI(track)))
    val playerScreenState: Flow<PlayerScreenState> = _playerScreenState

    private val _playlistsState: MutableStateFlow<PlaylistsState> =
        MutableStateFlow(PlaylistsState(isLoading = false))
    val playlistsState: StateFlow<PlaylistsState> = _playlistsState

    private val _playerTrackState: MutableStateFlow<PlayerTrackState> =
        MutableStateFlow(PlayerTrackState(inProgress = false))
    val playerTrackState: StateFlow<PlayerTrackState> = _playerTrackState

    init {
        playerInteractor.preparePlayer(track)

        viewModelScope.launch(Dispatchers.Default) {
            playerInteractor.stateFlow.collect { state ->
                when (state) {
                    is PlayerState.Paused, PlayerState.Prepared, PlayerState.Default -> _playerScreenState.value =
                        PlayerScreenState.Waiting(
                            track = TrackMapperUI.toTrackUI(track), progress = state.progress
                        )

                    is PlayerState.Playing -> _playerScreenState.value = PlayerScreenState.Playing(
                        track = TrackMapperUI.toTrackUI(track), progress = state.progress
                    )
                }
            }
        }
    }

    suspend fun loadPlaylists() = withContext(Dispatchers.IO) {
        _playlistsState.update { it.copy(isLoading = true) }
        val list = appDatabase.playlistDao().getPlaylists().first()

        val createdPlaylist = list.map { item ->
            CreatedPlaylist(
                id = item.id,
                name = item.name,
                trackCount = appDatabase.playlistDao().getTrackCount(item.id).firstOrNull() ?: 0,
                cover = loadFileUseCase(item.coverName.toString()).firstOrNull(),
                layout = R.layout.item_recyclerview_playlist_horizontal
            )
        }
        _playlistsState.update { it.copy(isLoading = false, list = createdPlaylist) }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteTrackInteractor.removeFavoriteTrack(track)
                track.isFavorite = false
            } else {
                favoriteTrackInteractor.addFavoriteTrack(track)
                track.isFavorite = true
            }
            _playerScreenState.update { it.copy(track = TrackMapperUI.toTrackUI(track)) }
        }
    }

    fun addToPlaylist(item: CreatedPlaylist) {
        _playerTrackState.update { it.copy(inProgress = true) }

        val playlistTrackEntity = track.toPlaylistTrackEntity()
        val playlistAndTrackEntity = PlaylistAndTrackEntity(
            playlistId = item.id, playlistTrackId = track.trackId
        )

        viewModelScope.launch(Dispatchers.IO) {
            val result = appDatabase
                .playlistDao()
                .containsInPlaylist(item.id, track.trackId)
                .firstOrNull()

            val isAdded = when (result == null) {
                true -> {
                    appDatabase.playlistDao().insertPlaylistTrack(playlistTrackEntity)
                    appDatabase.playlistDao().addToPlaylist(playlistAndTrackEntity)
                    true
                }

                false -> false
            }

            _playerTrackState.update {
                it.copy(
                    inProgress = false,
                    playlist = item,
                    isAdded = isAdded,
                    showSnackbar = true
                )
            }
        }
    }

    fun clearSnackbar() {
        _playerTrackState.update { it.copy(showSnackbar = false) }
    }

    fun playbackControl() {
        viewModelScope.launch(Dispatchers.Unconfined) {
            playerInteractor.playbackControl()
        }
    }

    fun playerPause() {
        playerInteractor.pause()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.stop()
    }
}