package com.example.playlistmaker.player.data

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : PlayerRepository {
    private val playerScope = CoroutineScope(Dispatchers.Default)
    private var updateJob: Job? = null

    private val playerState: MutableStateFlow<PlayerState> = MutableStateFlow(PlayerState.Default)

    override fun preparePlayer(track: Track) {
        if (track.previewUrl.isNullOrEmpty()) return

        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState.value = PlayerState.Prepared
        }
        mediaPlayer.setOnCompletionListener {
            updateJob?.cancel()
            playerState.value = PlayerState.Prepared
            mediaPlayer.seekTo(0)
        }
    }

    override fun startPlayer() {
        if (playerState.value is PlayerState.Default) return

        mediaPlayer.start()

        updateJob = playerScope.launch(Dispatchers.Default) {
            while (mediaPlayer.isPlaying) {
                delay(UPDATE_STATE_DELAY)
                playerState.value = PlayerState.Playing(getCurrentPosition())
            }
        }
    }

    override fun pausePlayer() {
        if (playerState.value !is PlayerState.Playing) return

        mediaPlayer.pause()
        updateJob?.cancel()
        playerState.value = PlayerState.Paused(getCurrentPosition())
    }

    override fun releasePlayer() {
        mediaPlayer.stop()
        mediaPlayer.release()
        playerState.value = PlayerState.Default
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun getCurrentState(): Flow<PlayerState> = playerState

    companion object {
        private const val UPDATE_STATE_DELAY = 300L
    }
}