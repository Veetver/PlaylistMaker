package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.domain.model.SearchTrackQuery

interface TracksInteractor {
    fun searchTracks(query: SearchTrackQuery, consumer: SearchResultConsumer)

    fun interface SearchResultConsumer {
        fun consume(searchResult: SearchResult)
    }
}