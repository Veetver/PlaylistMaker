package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.library.presentation.model.CreatedPlaylist

data class PlayerTrackState(
    val inProgress: Boolean = false,
    val playlist: CreatedPlaylist? = null,
    val isAdded: Boolean = false,
)