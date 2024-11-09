package com.example.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.player.presentation.model.TrackUI
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import com.example.playlistmaker.player.presentation.mapper.TrackMapper as TrackMapperUI

class PlayerViewModel(
    jsonTrack: String,
    gson: Gson,
    private val playerInteractor: PlayerInteractor,
) : ViewModel() {

    private val track = gson.fromJson(jsonTrack, Track::class.java)

    private val _trackLiveData = MutableLiveData(TrackMapperUI.toTrackUI(track))
    val trackLiveData: LiveData<TrackUI> = _trackLiveData

    private val _playerScreenStateLiveData: MutableLiveData<PlayerScreenState> =
        MutableLiveData(PlayerScreenState.Waiting)
    val playerScreenStateLiveData: LiveData<PlayerScreenState> = _playerScreenStateLiveData

    private val _playerProgressLiveData: MutableLiveData<Int> = MutableLiveData(0)
    val playerProgressLiveData: LiveData<Int> = _playerProgressLiveData

    init {
        playerInteractor.preparePlayer(track = track) { progress, state ->
            when (state) {
                PlayerState.STATE_PLAYING -> {
                    if (playerScreenStateLiveData.value != PlayerScreenState.Playing) {
                        _playerScreenStateLiveData.postValue(PlayerScreenState.Playing)
                    }
                    _playerProgressLiveData.postValue(progress)
                }

                PlayerState.STATE_PREPARED -> {
                    _playerProgressLiveData.postValue(progress)
                    _playerScreenStateLiveData.postValue(PlayerScreenState.Waiting)
                }

                PlayerState.STATE_DEFAULT,
                PlayerState.STATE_PAUSED -> {
                    _playerScreenStateLiveData.postValue(PlayerScreenState.Waiting)
                }
            }
        }
    }

    fun playbackControl() {
        playerInteractor.playbackControl()
    }

    fun playerPause() {
        playerInteractor.pause()
    }

    override fun onCleared() {
        super.onCleared()
        playerInteractor.stop()
    }
}