package com.example.playlistmaker.library.presentation.state

import com.example.playlistmaker.library.presentation.model.CreatedPlaylist

data class PlaylistsState(
    val isLoading: Boolean = false,
    val list: List<CreatedPlaylist> = emptyList()
)