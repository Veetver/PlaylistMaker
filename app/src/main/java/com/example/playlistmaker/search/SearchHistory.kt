package com.example.playlistmaker.search

import android.content.SharedPreferences
import com.example.playlistmaker.models.Track
import com.google.gson.Gson

class SearchHistory(private val prefs: SharedPreferences) {
    companion object {
        private const val SEARCH_HISTORY = "SEARCH_HISTORY"
    }

    private var searchHistory = mutableListOf<Track>()
    private val gson = Gson()

    init {
        searchHistory.addAll(getHistory())
    }

    fun getSearchHistory(): List<Track> = searchHistory

    fun saveHistory(list: List<Track>) {
        prefs.edit().putString(SEARCH_HISTORY, gson.toJson(list)).apply()
    }

    private fun getHistory(): Array<Track> {
        val json = prefs.getString(SEARCH_HISTORY, null) ?: return emptyArray()
        return gson.fromJson(json, Array<Track>::class.java)
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
}