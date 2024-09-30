package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.SearchResult
import com.example.playlistmaker.domain.model.SearchTrackQuery

interface TracksInteractor {
    fun searchTracks(query: SearchTrackQuery, consumer: SearchResultConsumer)

    fun interface SearchResultConsumer {
        fun consume(searchResult: SearchResult)
    }
}