package com.example.playlistmaker.player.presentation.state

sealed class PlayerScreenState {
    data object Waiting : PlayerScreenState()
    data object Playing : PlayerScreenState()
}