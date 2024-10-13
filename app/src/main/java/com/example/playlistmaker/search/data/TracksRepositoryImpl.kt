package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.mapper.TrackListMapper.toTrackList
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.SearchResult
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.TrackList

class TracksRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) : TracksRepository {

    override fun searchTracks(search: SearchTrackQuery): SearchResult {
        val response = remoteDataSource.doRequest(TrackSearchRequest(search.query))

        return when (response.resultCode) {
            200 -> {
                response as TrackSearchResponse
                SearchResult(true, toTrackList(response.results))
            }

            else -> SearchResult(false, TrackList(emptyList()))
        }
    }
}