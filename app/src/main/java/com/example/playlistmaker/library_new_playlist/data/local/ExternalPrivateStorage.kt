package com.example.playlistmaker.library_new_playlist.data.local

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.playlistmaker.library_new_playlist.data.LocalDataStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class ExternalPrivateStorage(
    private val context: Context
) : LocalDataStorage {
    override suspend fun saveFile(uri: Uri) {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_cover")

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val newFile = File(filePath, "${uri.lastPathSegment}")
        context
            .contentResolver
            .openInputStream(uri).use { inputStream ->
                if (inputStream != null) {
                    FileOutputStream(newFile).use { output ->
                        val buffer = ByteArray(4 * 1024)
                        while (true) {
                            val byteCount = inputStream.read(buffer)
                            if (byteCount < 0) break
                            output.write(buffer, 0, byteCount)
                        }
                        output.flush()
                        output.close()
                    }
                }
                inputStream?.close()
            }
    }

    override suspend fun loadFile(filename: String): Flow<File> = flow {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_cover")

        if (filePath.exists() && filename.isNotEmpty()) {
            val file = File(filePath, filename)
            emit(file)
        }
    }

}