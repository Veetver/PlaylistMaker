package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.RemoteDataSource
import com.example.playlistmaker.search.data.dto.Req
import com.example.playlistmaker.search.data.dto.Res
import com.example.playlistmaker.search.data.dto.TrackSearchRequest

class RetrofitDataSource(private val apiService: ITunesApiService) : RemoteDataSource {

    override fun doRequest(dto: Req): Res {
        return when {
            dto is TrackSearchRequest -> {
                try {
                    val res = apiService.searchTracks(dto.query).execute()
                    val body = res.body() ?: Res()
                    body.apply { resultCode = res.code() }
                } catch (ex: Exception) {
                    Res().apply { resultCode = 400 }
                }
            }

            else -> Res().apply { resultCode = 400 }
        }
    }
}