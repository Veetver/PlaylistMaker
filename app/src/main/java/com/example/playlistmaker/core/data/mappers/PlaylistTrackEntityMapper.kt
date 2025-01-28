package com.example.playlistmaker.core.data.mappers

import com.example.playlistmaker.core.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.search.domain.model.Track

object PlaylistTrackEntityMapper {
    fun PlaylistTrackEntity.toTrack() = Track(
        trackId = trackId,
        trackName = trackName,
        artistName = artistName,
        trackTime = trackTime,
        artworkUrl100 = artworkUrl100,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl
    )
}