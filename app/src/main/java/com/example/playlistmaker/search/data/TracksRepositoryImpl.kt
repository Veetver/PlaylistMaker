package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import com.example.playlistmaker.search.data.dto.TrackSearchResponse
import com.example.playlistmaker.search.data.mapper.TrackListMapper.toTrackList
import com.example.playlistmaker.search.domain.api.TracksRepository
import com.example.playlistmaker.search.domain.model.Resource
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.TrackList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
) : TracksRepository {

    override fun searchTracks(search: SearchTrackQuery): Flow<Resource<TrackList>> = flow {
        val response = remoteDataSource.doRequest(TrackSearchRequest(search.query))

        when (response.resultCode) {
            -2 -> emit(Resource.Error(response.resultCode))
            200 -> {
                response as TrackSearchResponse
                val data = toTrackList(response.results)
                emit(Resource.Success(data))
            }

            else -> emit(Resource.Error(response.resultCode))
        }
    }
}