package com.example.playlistmaker.library_new_playlist.presentation.state

import android.net.Uri

data class NewPlaylistState(
    val isCreated: Boolean, val details: NewPlaylistDetails
)

data class NewPlaylistDetails(
    val id: Long = 0,
    val name: String = "",
    val description: String = "",
    val uri: Uri = Uri.EMPTY
)