package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.data.mapper.TrackListMapper.toTrackList
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.model.SearchResult
import com.example.playlistmaker.domain.model.SearchTrackQuery
import com.example.playlistmaker.domain.model.TrackList

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