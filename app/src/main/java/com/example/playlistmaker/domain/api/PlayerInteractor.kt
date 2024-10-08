package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track

interface PlayerInteractor {
    fun preparePlayer(track: Track, consumer: PlayerProgressConsumer)
    fun playbackControl()
    fun pause()
    fun stop()

    fun interface PlayerProgressConsumer {
        fun consume(currentPositionInMillis: Int, state: PlayerState)
    }
}