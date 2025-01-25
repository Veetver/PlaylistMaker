package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.library.presentation.model.CreatedPlaylist

data class PlayerTrackState(
    val inProgress: Boolean = false,
    val isAdded: Boolean = false,
    val showSnackbar: Boolean = false,
    val playlist: CreatedPlaylist? = null,
)