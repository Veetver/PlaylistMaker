package com.example.playlistmaker.library_new_playlist.domain.impl

import android.net.Uri
import com.example.playlistmaker.library_new_playlist.data.LocalDataStorage

class SaveFileUseCase(
    private val storage: LocalDataStorage
) {
    suspend operator fun invoke(uri: Uri) = storage.saveFile(uri)
}