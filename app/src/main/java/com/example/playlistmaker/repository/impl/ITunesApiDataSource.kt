package com.example.playlistmaker.repository.impl

import android.util.Log
import com.example.playlistmaker.api.itunes.ITunesApi
import com.example.playlistmaker.api.itunes.models.SearchResponse
import com.example.playlistmaker.api.itunes.models.toTrack
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.repository.interfaces.RemoteTracksDataSource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ITunesApiDataSource : RemoteTracksDataSource {

    companion object {
        const val BASE_URL = "https://itunes.apple.com"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ITunesApi::class.java)


    override fun search(text: String, onSuccess: (List<Track>) -> Unit, onFailure: () -> Unit) {
        api.search(text).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.code() == 200) {
                    if (response.body()?.results?.isNotEmpty() == true) {
                        val result = response.body()?.results!!
                            .map { trackApi -> trackApi.toTrack() }
                        onSuccess(result)
                    } else {
                        onSuccess(emptyList())
                    }
                } else onFailure()
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                onFailure()
            }
        })
    }
}