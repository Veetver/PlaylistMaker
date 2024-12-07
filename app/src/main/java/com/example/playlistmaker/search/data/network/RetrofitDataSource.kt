package com.example.playlistmaker.search.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.search.data.RemoteDataSource
import com.example.playlistmaker.search.data.dto.Req
import com.example.playlistmaker.search.data.dto.Res
import com.example.playlistmaker.search.data.dto.TrackSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitDataSource(
    private val apiService: ITunesApiService,
    private val context: Context,
) : RemoteDataSource {

    override suspend fun doRequest(dto: Req): Res {
        if (!isConnected()) {
            return Res().apply { resultCode = -2 }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = when (dto) {
                    is TrackSearchRequest -> apiService.searchTracks(dto.query)
                    else -> Res().apply { resultCode = 400 }
                }

                if (response.resultCode == -1) {
                    response.apply {
                        resultCode = 200
                    }
                }

                return@withContext response
            } catch (e: Throwable) {
                Res().apply { resultCode = 500 }
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }
}