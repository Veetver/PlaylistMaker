package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newSingleThreadExecutor()

    override fun searchTracks(
        query: SearchTrackQuery,
        consumer: TracksInteractor.SearchResultConsumer
    ) {
        executor.execute {
            consumer.consume(repository.searchTracks(query))
        }
    }
}