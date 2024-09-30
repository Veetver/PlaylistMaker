package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.model.SearchTrackQuery
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