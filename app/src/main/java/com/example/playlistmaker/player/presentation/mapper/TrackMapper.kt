package com.example.playlistmaker.player.presentation.mapper

import com.example.playlistmaker.player.presentation.model.TrackUI
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object TrackMapper {
    fun toTrackUI(track: Track): TrackUI {
        return TrackUI(
            trackName = track.trackName ?: "N/A",
            artistName = track.artistName ?: "N/A",
            trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime) ?: "N/A",
            artworkUrl512 = track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg") ?: "",
            collectionName = track.collectionName ?: "",
            releaseDate = if (!track.releaseDate.isNullOrEmpty())
                LocalDate.parse(
                    track.releaseDate,
                    DateTimeFormatter.ISO_DATE_TIME
                ).year.toString() else "N/A",
            primaryGenreName = track.primaryGenreName ?: "N/A",
            country = track.country ?: "N/A",
            previewUrl = track.previewUrl ?: "N/A",
            isFavorite = track.isFavorite
        )
    }
}