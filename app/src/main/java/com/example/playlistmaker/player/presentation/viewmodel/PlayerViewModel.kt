package com.example.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.playlistmaker.player.presentation.mapper.TrackMapper as TrackMapperUI

class PlayerViewModel(
    jsonTrack: String,
    gson: Gson,
    private val playerInteractor: PlayerInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
) : ViewModel() {

    private val track = gson.fromJson(jsonTrack, Track::class.java)

    private val _playerScreenState: MutableStateFlow<PlayerScreenState> =
        MutableStateFlow(PlayerScreenState.Initializing(TrackMapperUI.toTrackUI(track)))
    val playerScreenState: Flow<PlayerScreenState> = _playerScreenState

    init {
        playerInteractor.preparePlayer(track)

        viewModelScope.launch(Dispatchers.Default) {
            playerInteractor.stateFlow.onEach { state ->
                    when (state) {
                        is PlayerState.Paused, PlayerState.Prepared, PlayerState.Default -> _playerScreenState.value =
                            PlayerScreenState.Waiting(
                                track = TrackMapperUI.toTrackUI(track), progress = state.progress
                            )

                        is PlayerState.Playing -> _playerScreenState.value =
                            PlayerScreenState.Playing(
                                track = TrackMapperUI.toTrackUI(track), progress = state.progress
                            )
                    }
                }.collect()
        }
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