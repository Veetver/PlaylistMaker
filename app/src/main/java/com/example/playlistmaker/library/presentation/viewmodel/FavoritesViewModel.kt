package com.example.playlistmaker.library.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.presentation.state.FavoritesScreenState
import com.example.playlistmaker.player.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList
import com.example.playlistmaker.util.SingleLiveEvent
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val gson: Gson,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val tracksHistoryInteractor: TrackHistoryInteractor,
) : ViewModel() {

    private val _favoritesScreenState: MutableStateFlow<FavoritesScreenState> = MutableStateFlow(FavoritesScreenState.Loading)
    val favoritesScreenState: StateFlow<FavoritesScreenState> = _favoritesScreenState

    private val _showTrackTrigger = SingleLiveEvent<String>()
    val showTrackTrigger: LiveData<String> = _showTrackTrigger

    init {
        updateState()
    }

    fun updateState() {
        viewModelScope.launch(Dispatchers.Default) {
            favoriteTrackInteractor
                .getTrackList()
                .collect{ trackList ->
                    process(trackList)
                }
        }
    }

    private fun process(trackList: TrackList) {
        _favoritesScreenState.value = if (trackList.list.isEmpty()) FavoritesScreenState.Empty else FavoritesScreenState.Content(trackList)
    }

    fun onItemClick(track: Track) {
        viewModelScope.launch(Dispatchers.Default) {
            favoriteTrackInteractor
                .isFavorite(track)
                .collect { track ->
                    tracksHistoryInteractor.addTrack(track)
                    _showTrackTrigger.postValue(gson.toJson(track))
                }
        }
    }
}