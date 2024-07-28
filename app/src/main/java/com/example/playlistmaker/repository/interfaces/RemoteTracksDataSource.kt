package com.example.playlistmaker.repository.interfaces

import com.example.playlistmaker.models.Track

interface RemoteTracksDataSource {
    fun search(text: String, onSuccess: (List<Track>) -> Unit, onFailure: () -> Unit)
}