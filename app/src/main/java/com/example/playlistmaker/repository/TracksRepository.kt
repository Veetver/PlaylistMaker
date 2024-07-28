package com.example.playlistmaker.repository

import com.example.playlistmaker.models.Track
import com.example.playlistmaker.repository.interfaces.*

class TracksRepository(
    private val remoteTracksDataSource: RemoteTracksDataSource,
) {
    fun search(text: String, onSuccess: (List<Track>) -> Unit, onFailure: () -> Unit) {
        remoteTracksDataSource.search(text, onSuccess, onFailure)
    }
}