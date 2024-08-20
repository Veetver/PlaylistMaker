package com.example.playlistmaker.models

data class Track(
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
)