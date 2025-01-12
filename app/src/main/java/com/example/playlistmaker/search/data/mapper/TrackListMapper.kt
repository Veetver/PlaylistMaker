package com.example.playlistmaker.search.data.mapper

import com.example.playlistmaker.core.data.mappers.TrackApiDtoMapper.toTrack
import com.example.playlistmaker.core.data.mappers.TrackDtoMapper.toTrack
import com.example.playlistmaker.core.data.mappers.TrackMapper.toTrackDto
import com.example.playlistmaker.search.data.dto.TrackApiDto
import com.example.playlistmaker.search.data.dto.TrackListDto
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList

object TrackListMapper {
    fun toTrackList(dto: TrackListDto): TrackList {
        return TrackList(dto.list.map { it.toTrack() })
    }

    fun toTrackList(dto: List<TrackApiDto>): TrackList {
        return TrackList(dto.map { it.toTrack() })
    }

    fun toTrackListDto(model: List<Track>): TrackListDto {
        return TrackListDto(model.map { it.toTrackDto() })
    }
}