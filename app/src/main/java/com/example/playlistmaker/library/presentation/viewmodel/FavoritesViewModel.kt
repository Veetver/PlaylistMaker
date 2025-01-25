package com.example.playlistmaker.library.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.library.presentation.state.FavoritesScreenState
import com.example.playlistmaker.player.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val tracksHistoryInteractor: TrackHistoryInteractor,
) : ViewModel() {

    private val _favoritesScreenState: MutableStateFlow<FavoritesScreenState> =
        MutableStateFlow(FavoritesScreenState.Loading)
    val favoritesScreenState: StateFlow<FavoritesScreenState> = _favoritesScreenState

    private val _showTrackTrigger = SingleLiveEvent<Track>()
    val showTrackTrigger: LiveData<Track> = _showTrackTrigger

    init {
        updateState()
    }

    fun updateState() {
        viewModelScope.launch(Dispatchers.Default) {
            favoriteTrackInteractor
                .getTracks()
                .collect { list ->
                    process(list)
                }
        }
    }

    private fun process(trackList: List<Track>) {
        _favoritesScreenState.value =
            if (trackList.isEmpty()) FavoritesScreenState.Empty else FavoritesScreenState.Content(
                trackList
            )
    }

    fun onItemClick(track: Track) {
        viewModelScope.launch(Dispatchers.Default) {
            favoriteTrackInteractor
                .isFavorite(track)
                .collect { track ->
                    tracksHistoryInteractor.addTrack(track)
                    _showTrackTrigger.postValue(track)
                }
        }
    }
}