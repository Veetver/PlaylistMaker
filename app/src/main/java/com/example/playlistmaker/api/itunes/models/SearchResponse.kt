package com.example.playlistmaker.api.itunes.models

data class SearchResponse(
    val resultCount: Int,
    val results: List<TrackApi>
)