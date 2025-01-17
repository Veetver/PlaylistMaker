package com.example.playlistmaker.core.data.mappers

import com.example.playlistmaker.core.data.db.entity.TrackEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.model.Track

object TrackEntityMapper {
    fun TrackEntity.toTrackDto(): TrackDto {
        return TrackDto(
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

    fun TrackEntity.toTrack(): Track {
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
}