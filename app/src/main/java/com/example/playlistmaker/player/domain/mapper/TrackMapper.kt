package com.example.playlistmaker.player.domain.mapper

import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import org.koin.core.component.KoinComponent

object TrackMapper : KoinComponent {
    private val gson: Gson = getKoin().get()

    fun toTrack(json: String): Track {
        return gson.fromJson(json, Track::class.java)
    }
}