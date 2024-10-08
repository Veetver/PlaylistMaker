package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track

class PlayerInteractorImpl(
    private val playerRepository: PlayerRepository
) : PlayerInteractor {
    private var isRunning = true
    private val lock = Any()

    override fun preparePlayer(track: Track, consumer: PlayerInteractor.PlayerProgressConsumer) {
        playerRepository.preparePlayer(track)
        startLooper(consumer)
    }

    override fun playbackControl() {
        when (playerRepository.getCurrentState()) {
            PlayerState.STATE_PLAYING -> {
                playerRepository.pausePlayer()
                synchronized(lock) {
                    (lock as Object).notify()
                }
            }

            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                playerRepository.startPlayer()
                synchronized(lock) {
                    (lock as Object).notify()
                }
            }

            PlayerState.STATE_DEFAULT -> {}
        }
    }

    private fun startLooper(consumer: PlayerInteractor.PlayerProgressConsumer) {
        val playerRunnable = Runnable {
            while (isRunning) {
                when (playerRepository.getCurrentState()) {
                    PlayerState.STATE_PLAYING -> {
                        consumer.consume(
                            currentPositionInMillis = playerRepository.getCurrentPosition(),
                            state = playerRepository.getCurrentState()
                        )
                        Thread.sleep(300)
                    }

                    PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED, PlayerState.STATE_DEFAULT -> {
                        consumer.consume(
                            currentPositionInMillis = playerRepository.getCurrentPosition(),
                            state = playerRepository.getCurrentState()
                        )
                        synchronized(lock) {
                            (lock as Object).wait()
                        }
                    }
                }

            }
        }

        Thread(playerRunnable).start()
    }

    override fun pause() {
        playerRepository.pausePlayer()
    }

    override fun stop() {
        isRunning = false
        playerRepository.releasePlayer()
    }
}