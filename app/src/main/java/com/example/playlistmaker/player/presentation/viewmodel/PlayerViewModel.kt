package com.example.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.presentation.model.TrackUI
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.example.playlistmaker.player.presentation.mapper.TrackMapper as TrackMapperUI

class PlayerViewModel(
    jsonTrack: String,
    gson: Gson,
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    private val track = gson.fromJson(jsonTrack, Track::class.java)

    private val _trackLiveData = MutableLiveData(TrackMapperUI.toTrackUI(track))
    val trackLiveData: LiveData<TrackUI> = _trackLiveData

    val playerState: Flow<PlayerState> = flow {
        playerInteractor
            .stateFlow
            .onEach {
                _playerScreenState.value = when(it) {
                    is PlayerState.Paused, PlayerState.Prepared, PlayerState.Default -> PlayerScreenState.Waiting
                    is PlayerState.Playing -> PlayerScreenState.Playing
                }
            }
            .collect{ emit(it) }
    }
    private val _playerScreenState: MutableStateFlow<PlayerScreenState> = MutableStateFlow(PlayerScreenState.Waiting)
    val playerScreenState: Flow<PlayerScreenState> = _playerScreenState

    init {
        playerInteractor.preparePlayer(track)
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