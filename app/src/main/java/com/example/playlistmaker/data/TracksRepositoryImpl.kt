package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.dto.toTrack
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.model.SearchResult
import com.example.playlistmaker.domain.model.SearchTrackQuery
import com.example.playlistmaker.domain.model.TrackList
import com.example.playlistmaker.domain.model.toTrackListDto

class TracksRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : TracksRepository {

    override fun searchTracks(search: SearchTrackQuery): SearchResult {
        val response = remoteDataSource.doRequest(TrackSearchRequest(search.query))

        return when (response.resultCode) {
            200 -> {
                response as TrackSearchResponse
                SearchResult(true, TrackList(response.results.map { it.toTrack() }))
            }

            else -> SearchResult(false, TrackList(emptyList()))
        }
    }

    override fun getSearchHistory(): TrackList =
        TrackList(localDataSource.getSearchHistory().list.map { it.toTrack() })

    override fun saveSearchHistory(history: TrackList): Boolean =
        localDataSource.saveSearchHistory(history.toTrackListDto())

    override fun clearSearchHistory(): Boolean =
        localDataSource.clearSearchHistory()
}