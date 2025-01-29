package com.example.playlistmaker.library_new_playlist.presentation.viewmodel

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library_new_playlist.domain.impl.LoadFileUseCase
import com.example.playlistmaker.library_new_playlist.domain.impl.SaveFileUseCase
import com.example.playlistmaker.library_new_playlist.presentation.state.NewPlaylistDetails
import com.example.playlistmaker.library_new_playlist.presentation.state.NewPlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val saveFileUseCase: SaveFileUseCase,
    private val appDatabase: AppDatabase,
    private val loadFileUseCase: LoadFileUseCase,
) : ViewModel() {
    private val _newPlaylistScreenState: MutableStateFlow<NewPlaylistState> =
        MutableStateFlow(NewPlaylistState(false, NewPlaylistDetails()))
    val newPlaylistScreenState: StateFlow<NewPlaylistState> = _newPlaylistScreenState


    fun setPlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase
                .playlistDao()
                .getPlaylist(playlistId)
                .firstOrNull()?.let { playlist ->
                    _newPlaylistScreenState.update {
                        it.copy(
                            details = NewPlaylistDetails(
                                id = playlist.id,
                                name = playlist.name,
                                description = playlist.description ?: "",
                                uri = loadFileUseCase(playlist.coverName.toString()).firstOrNull()?.toUri() ?: Uri.EMPTY
                            )
                        )
                    }
                }
        }
    }

    fun setCover(uri: Uri) {
        _newPlaylistScreenState.update { it.copy(details = it.details.copy(uri = uri)) }
    }

    fun upsertPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = newPlaylistScreenState.value
            val isUriEmpty = state.details.uri == Uri.EMPTY
            val playlistEntity = PlaylistEntity(
                id = state.details.id,
                name = state.details.name,
                description = state.details.description,
                coverName = if (!isUriEmpty) state.details.uri.lastPathSegment else ""
            )

            if (!isUriEmpty) {
                saveFileUseCase(state.details.uri)
            }

            appDatabase
                .playlistDao()
                .upsert(playlistEntity)

            _newPlaylistScreenState.update { it.copy(isCreated = true) }
        }
    }

    fun setName(text: String) {
        _newPlaylistScreenState.update {
            it.copy(details = it.details.copy(name = text))
        }
    }

    fun setDescription(text: String) {
        _newPlaylistScreenState.update {
            it.copy(details = it.details.copy(description = text))
        }
    }
}