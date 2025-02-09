package com.example.playlistmaker.playlist_details.presentation.model

import java.io.File

data class PlaylistDetailsUI(
    val name: String,
    val description: String?,
    val totalTrackTime: Int,
    val trackCount: Int,
    val cover: File?,
)
