package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.SearchResult
import com.example.playlistmaker.domain.model.SearchTrackQuery
import com.example.playlistmaker.domain.model.TrackList

interface TracksInteractor {
    fun searchTracks(query: SearchTrackQuery, consumer: SearchResultConsumer)
    fun getTracksHistory(consumer: TracksConsumer)
    fun saveTrackHistory(trackList: TrackList)
    fun clearTrackHistory()

    fun interface TracksConsumer {
        fun consume(trackList: TrackList)
    }

    fun interface SearchResultConsumer {
        fun consume(searchResult: SearchResult)
    }
}