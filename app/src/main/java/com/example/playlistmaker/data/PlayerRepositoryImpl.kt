package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track

class PlayerRepositoryImpl() : PlayerRepository {

    private val player = MediaPlayer()
    private var playerState = PlayerState.STATE_DEFAULT

    override fun preparePlayer(track: Track) {
        player.setDataSource(track.previewUrl)
        player.prepareAsync()
        player.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
        player.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED

        }
    }

    override fun startPlayer() {
        player.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        player.pause()
        playerState = PlayerState.STATE_PAUSED
    }

    override fun releasePlayer() {
        player.release()
        playerState = PlayerState.STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int {
        return player.currentPosition
    }

    override fun getCurrentState(): PlayerState {
        return playerState
    }
}