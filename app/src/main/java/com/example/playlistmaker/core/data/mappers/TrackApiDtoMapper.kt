package com.example.playlistmaker.core.data.mappers

import com.example.playlistmaker.search.data.dto.TrackApiDto
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackApiDtoMapper {
    fun TrackApiDto.toTrack() = Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(this.trackTimeMillis),
        artworkUrl100 = this.artworkUrl100,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl,
    )
}