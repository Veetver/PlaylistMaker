package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.RemoteDataSource
import com.example.playlistmaker.data.dto.Req
import com.example.playlistmaker.data.dto.Res
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitDataSource : RemoteDataSource {

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ITunesApiService::class.java)


    override fun doRequest(dto: Req): Res {
        return when {
            dto is TrackSearchRequest -> {
                try {
                    val res = api.searchTracks(dto.query).execute()
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