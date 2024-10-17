package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track

interface PlayerInteractor {
    fun preparePlayer(track: Track, consumer: PlayerProgressConsumer)
    fun playbackControl()
    fun pause()
    fun stop()

    fun interface PlayerProgressConsumer {
        fun consume(currentPositionInMillis: Int, state: PlayerState)
    }
}