package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.search.data.dto.TrackApiDto
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun toTrack(dto: TrackDto): Track {
        return Track(
            trackId = dto.trackId,
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTime = dto.trackTime,
            artworkUrl100 = dto.artworkUrl100,
            collectionName = dto.collectionName,
            releaseDate = dto.releaseDate,
            primaryGenreName = dto.primaryGenreName,
            country = dto.country,
            previewUrl = dto.previewUrl,
        )
    }

    fun toTrack(dto: TrackApiDto): Track {
        return Track(
            trackId = dto.trackId,
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(dto.trackTimeMillis),
            artworkUrl100 = dto.artworkUrl100,
            collectionName = dto.collectionName,
            releaseDate = dto.releaseDate,
            primaryGenreName = dto.primaryGenreName,
            country = dto.country,
            previewUrl = dto.previewUrl,
        )
    }

    fun toTrackDto(model: Track): TrackDto {
        return TrackDto(
            trackId = model.trackId,
            trackName = model.trackName,
            artistName = model.artistName,
            trackTime = model.trackTime,
            artworkUrl100 = model.artworkUrl100,
            collectionName = model.collectionName,
            releaseDate = model.releaseDate,
            primaryGenreName = model.primaryGenreName,
            country = model.country,
            previewUrl = model.previewUrl,
        )
    }
}