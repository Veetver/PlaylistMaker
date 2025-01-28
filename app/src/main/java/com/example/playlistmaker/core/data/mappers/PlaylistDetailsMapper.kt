package com.example.playlistmaker.core.data.mappers

import com.example.playlistmaker.playlist_details.presentation.model.PlaylistDetailsUI
import com.example.playlistmaker.playlist_details.presentation.state.PlaylistDetails
import java.text.SimpleDateFormat
import java.util.Locale

object PlaylistDetailsMapper {
    fun PlaylistDetails.toPlaylistDetailsUI(): PlaylistDetailsUI = PlaylistDetailsUI(
        name = name,
        description = description,
        totalTrackTime = SimpleDateFormat("mm", Locale.getDefault()).format(trackList.sumOf { it.trackTime?.toInt() ?: 0 }).toInt(),
        trackCount = trackList.size,
        cover = cover
    )
}