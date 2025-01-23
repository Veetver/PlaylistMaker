package com.example.playlistmaker.library_new_playlist.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library_new_playlist.domain.impl.SaveFileUseCase
import com.example.playlistmaker.library_new_playlist.presentation.state.NewPlaylistDetails
import com.example.playlistmaker.library_new_playlist.presentation.state.NewPlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val saveFileUseCase: SaveFileUseCase,
    private val appDatabase: AppDatabase,
) : ViewModel() {
    private val _newPlaylistScreenState: MutableStateFlow<NewPlaylistState> =
        MutableStateFlow(NewPlaylistState(false, NewPlaylistDetails()))
    val newPlaylistScreenState: StateFlow<NewPlaylistState> = _newPlaylistScreenState


    fun setCover(uri: Uri) {
        _newPlaylistScreenState.update { it.copy(details = it.details.copy(uri = uri)) }
    }

    fun createPlaylist() {
        viewModelScope.launch(Dispatchers.IO) {
            val state = newPlaylistScreenState.value
            val isUriEmpty = state.details.uri == Uri.EMPTY
            val playlistEntity = PlaylistEntity(
                name = state.details.name,
                description = state.details.description,
                coverName = if (!isUriEmpty) state.details.uri.lastPathSegment else ""
            )

            if (!isUriEmpty) {
                saveFileUseCase(state.details.uri)
            }

            appDatabase
                .playlistDao()
                .insert(playlistEntity)

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