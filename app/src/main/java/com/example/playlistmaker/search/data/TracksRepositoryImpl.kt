package com.example.playlistmaker.search.data

import com.example.playlistmaker.core.data.mappers.TrackApiDtoMapper.toTrack
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) : TracksRepository {

    override fun searchTracks(search: SearchTrackQuery): Flow<Resource<List<Track>>> = flow {
        val response = remoteDataSource.doRequest(TrackSearchRequest(search.query))
        when (response.resultCode) {
            200 -> {
                response as TrackSearchResponse
                val data = response.results.map { it.toTrack() }
                emit(Resource.Success(data))
            }

            else -> emit(Resource.Error(response.resultCode))
        }
    }
}