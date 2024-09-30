package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track

interface PlayerRepository {
    fun preparePlayer(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
    fun getCurrentState(): PlayerState
}