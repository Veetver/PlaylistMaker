package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.LocalDataSource
import com.example.playlistmaker.data.mapper.TrackListMapper.toTrackList
import com.example.playlistmaker.data.mapper.TrackListMapper.toTrackListDto
import com.example.playlistmaker.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackList

class TrackHistoryInteractorImpl(
    private val local: LocalDataSource
) : TrackHistoryInteractor {

    override fun addTrack(track: Track) {

        val history: MutableList<Track> = getHistory().list.toMutableList()

        history.remove(track)
        history.add(0, track)

        if (history.size > 10) {
            history.removeLast()
        }
        local.saveSearchHistory(toTrackListDto(history))
    }

    override fun getHistory(): TrackList {
        return toTrackList(local.getSearchHistory())
    }

    override fun clearHistory() {
        local.clearSearchHistory()
    }

}