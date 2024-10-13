package com.example.playlistmaker.player.domain.model

enum class PlayerState(val value: Int) {
    STATE_DEFAULT(0),
    STATE_PREPARED(1),
    STATE_PLAYING(2),
    STATE_PAUSED(3),
}