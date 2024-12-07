package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.api.PlayerInteractor
import com.example.playlistmaker.player.domain.api.PlayerRepository
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.first

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository,
) : PlayerInteractor {

    override val stateFlow = playerRepository.getCurrentState()

    override fun preparePlayer(track: Track) = playerRepository.preparePlayer(track)

    override suspend fun playbackControl() {
        when (stateFlow.first()) {
            is PlayerState.Playing -> {
                playerRepository.pausePlayer()
            }

            is PlayerState.Prepared, is PlayerState.Paused -> {
                playerRepository.startPlayer()
            }

            is PlayerState.Default -> {}
        }
    }

    override fun pause() {
        playerRepository.pausePlayer()
    }

    override fun stop() {
        playerRepository.releasePlayer()
    }
}