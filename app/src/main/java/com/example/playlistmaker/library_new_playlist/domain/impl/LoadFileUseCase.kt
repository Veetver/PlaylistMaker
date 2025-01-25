package com.example.playlistmaker.library_new_playlist.domain.impl

import com.example.playlistmaker.library_new_playlist.data.LocalDataStorage

class LoadFileUseCase(
    private val storage: LocalDataStorage
) {
    suspend operator fun invoke(coverUri: String) = storage.loadFile(coverUri)
}