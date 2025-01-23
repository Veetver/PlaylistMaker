package com.example.playlistmaker.library_new_playlist.data

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import java.io.File

interface LocalDataStorage {
    suspend fun saveFile(uri: Uri)
    suspend fun loadFile(filename: String): Flow<File>
}