package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track


class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {
    private var playerState = PlayerState.STATE_DEFAULT

    override fun preparePlayer(track: Track): Boolean {
        if (track.previewUrl.isNullOrEmpty()) return false

        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            mediaPlayer.seekTo(0)
        }

        return true
    }

    override fun startPlayer() {
        if(playerState == PlayerState.STATE_DEFAULT) return

        mediaPlayer.start()
        playerState = PlayerState.STATE_PLAYING
    }

    override fun pausePlayer() {
        if (getCurrentState() != PlayerState.STATE_PLAYING) return

        mediaPlayer.pause()
        playerState = PlayerState.STATE_PAUSED

    }

    override fun releasePlayer() {
        mediaPlayer.release()
        playerState = PlayerState.STATE_DEFAULT
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getCurrentState(): PlayerState {
        return playerState
    }
}