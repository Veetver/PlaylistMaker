package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.dto.TrackApiDto
import com.example.playlistmaker.data.dto.TrackListDto
import com.example.playlistmaker.data.mapper.TrackMapper.toTrackDto
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackList

object TrackListMapper {
    fun toTrackList(dto: TrackListDto): TrackList {
        return TrackList(dto.list.map { TrackMapper.toTrack(it) })
    }

    fun toTrackList(dto: List<TrackApiDto>): TrackList {
        return TrackList(dto.map { TrackMapper.toTrack(it) })
    }

    fun toTrackListDto(model: List<Track>): TrackListDto {
        return TrackListDto(model.map { toTrackDto(it) })
    }
}