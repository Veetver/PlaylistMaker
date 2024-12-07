package com.example.playlistmaker.player.presentation.state

sealed interface PlayerScreenState {
    data object Waiting : PlayerScreenState
    data object Playing : PlayerScreenState
}