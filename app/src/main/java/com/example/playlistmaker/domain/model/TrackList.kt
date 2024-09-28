package com.example.playlistmaker.domain.model

import com.example.playlistmaker.data.dto.TrackListDto

data class TrackList(val list: List<Track>)

fun TrackList.toTrackListDto(): TrackListDto {
    return TrackListDto(this.list.map { it.toTrackDto() })
}
