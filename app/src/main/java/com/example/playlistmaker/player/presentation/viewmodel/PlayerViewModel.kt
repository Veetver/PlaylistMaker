package com.example.playlistmaker.player.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.player.presentation.state.PlayerScreenState

class PlayerViewModel(
    private val track: Track,
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    private val _trackLiveData = MutableLiveData(track)
    val trackLiveData: LiveData<Track> = _trackLiveData

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

                PlayerState.STATE_DEFAULT,
                PlayerState.STATE_PREPARED,
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

    companion object {
        fun getViewModelFactory(jsonTrack: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val gson = Creator.provideGson()
                val playerInteractor = Creator.proidePlayerInterator()

                PlayerViewModel(
                    track = gson.fromJson(jsonTrack, Track::class.java),
                    playerInteractor = playerInteractor,
                )
            }
        }
    }
}