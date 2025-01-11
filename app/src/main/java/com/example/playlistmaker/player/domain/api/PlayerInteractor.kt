package com.example.playlistmaker.player.domain.api

import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {
    val stateFlow: Flow<PlayerState>
    fun preparePlayer(track: Track)
    suspend fun playbackControl()
    fun pause()
    fun stop()

//    fun interface PlayerProgressConsumer {
//        fun consume(currentPositionInMillis: Int, state: PlayerState)
//    }
}