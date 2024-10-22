package com.example.playlistmaker.search.data.local

import android.content.Context
import com.example.playlistmaker.search.data.LocalDataSource
import com.example.playlistmaker.search.data.dto.TrackListDto
import com.google.gson.Gson

class SharedPrefsDataSource(
    context: Context,
    private val gson: Gson
) : LocalDataSource {
    private val prefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    override fun getSearchHistory(): TrackListDto {
        val searchHistoryJson =
            prefs.getString(SEARCH_HISTORY, null) ?: return TrackListDto(emptyList())
        return gson.fromJson(searchHistoryJson, TrackListDto::class.java)
    }

    override fun saveSearchHistory(history: TrackListDto): Boolean {
        prefs.edit().putString(SEARCH_HISTORY, gson.toJson(history)).apply()
        return true
    }

    override fun clearSearchHistory(): Boolean {
        prefs.edit().remove(SEARCH_HISTORY).apply()
        return true
    }

    companion object {
        const val SHARED_PREFS = "com.example.playlistmaker"
        private const val SEARCH_HISTORY = "SEARCH_HISTORY"
    }
}