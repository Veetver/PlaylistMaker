package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.model.Track

data class TrackDto(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String?,
    val artworkUrl100: String?,
    val collectionName: String,
    val releaseDate: String?,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String?,
)

fun TrackDto.toTrack(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        artworkUrl100 = this.artworkUrl100,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl,
    )
}
