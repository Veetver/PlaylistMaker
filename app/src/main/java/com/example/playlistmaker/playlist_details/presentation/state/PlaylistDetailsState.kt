package com.example.playlistmaker.playlist_details.presentation.state

import com.example.playlistmaker.search.domain.model.Track
import java.io.File

data class PlaylistDetailsState(
    val isLoading: Boolean = false,
    val playlist: PlaylistDetails? = null,
)

data class PlaylistDetails(
    val name: String = "",
    val trackCount: Int = 0,
    val cover: File? = null,
    val description: String? = "",
    val trackList: List<Track> = emptyList(),
)
