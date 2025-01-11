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
        if (!isConnected()) return Res().apply { resultCode = -1 }

        return withContext(Dispatchers.IO) {
            try {
                return@withContext when (dto) {
                    is TrackSearchRequest -> apiService.searchTracks(dto.query).apply { resultCode = 200 }
                    else -> Res().apply { resultCode = 400 }
                }
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