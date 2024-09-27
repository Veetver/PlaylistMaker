package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.model.SearchTrackQuery
import com.example.playlistmaker.domain.model.TrackList
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

    override fun getTracksHistory(consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.getSearchHistory())
        }
    }

    override fun saveTrackHistory(trackList: TrackList) {
        executor.execute {
            repository.saveSearchHistory(trackList)
        }
    }

    override fun clearTrackHistory() {
        executor.execute {
            repository.clearSearchHistory()
        }
    }
}