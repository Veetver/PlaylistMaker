package com.example.playlistmaker.library.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.presentation.model.CreatedPlaylist
import com.example.playlistmaker.library.presentation.state.PlaylistsState
import com.example.playlistmaker.library_new_playlist.domain.impl.LoadFileUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val appDatabase: AppDatabase,
    private val loadFileUseCase: LoadFileUseCase,
) : ViewModel() {
    private val _playlistsScreenState: MutableStateFlow<PlaylistsState> =
        MutableStateFlow(PlaylistsState(isLoading = true))
    val playlistsScreenState = _playlistsScreenState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            appDatabase
                .playlistDao()
                .getPlaylists()
                .collect { list ->
                    updateState(list)
                }
        }
    }

    fun updatePlaylists() {
        _playlistsScreenState.update { it.copy(isLoading = true) }

        viewModelScope.launch(Dispatchers.IO) {
            appDatabase
                .playlistDao()
                .getPlaylists()
                .first().let { list ->
                    updateState(list)
                }
        }
    }

    private suspend fun updateState(list: List<PlaylistEntity>) {
        _playlistsScreenState.update {
            it.copy(isLoading = false, list = list.map { item ->
                CreatedPlaylist(
                    item.id,
                    item.name,
                    item.description,
                    appDatabase.playlistDao().getTrackCount(item.id)
                        .firstOrNull() ?: 0,
                    loadFileUseCase(item.coverName.toString()).firstOrNull(),
                    R.layout.item_recyclerview_playlist
                )
            })
        }
    }
}