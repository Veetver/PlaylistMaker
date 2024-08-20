package com.example.playlistmaker.api.itunes.models

import com.example.playlistmaker.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

data class TrackApi(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String
)

fun TrackApi.toTrack(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(this.trackTimeMillis),
        artworkUrl100 = this.artworkUrl100,
    )
}