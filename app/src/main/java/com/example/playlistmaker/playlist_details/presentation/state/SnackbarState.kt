package com.example.playlistmaker.playlist_details.presentation.state

data class SnackbarState(
    val isNeedShow: Boolean = false,
    val text: String = "",
)