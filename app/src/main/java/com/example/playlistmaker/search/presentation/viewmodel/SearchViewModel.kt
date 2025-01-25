package com.example.playlistmaker.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.player.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.state.SearchScreenState
import com.example.playlistmaker.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val tracksHistoryInteractor: TrackHistoryInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
) : ViewModel() {

    private val _searchScreenState: MutableLiveData<SearchScreenState> =
        MutableLiveData(SearchScreenState.Default)
    val searchScreenState: LiveData<SearchScreenState> = _searchScreenState

    private val _showTrackTrigger = SingleLiveEvent<Track>()
    val showTrackTrigger: LiveData<Track> = _showTrackTrigger

    private val lastSearchQuery: MutableLiveData<SearchTrackQuery?> = MutableLiveData()

    private var searchJob: Job? = null

    fun retrySearch() {
        val searchQuery = lastSearchQuery.value
        if (searchQuery != null) {
            searchDebounce(searchQuery)
        }
    }

    private fun searchDebounce(query: SearchTrackQuery) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.Default) {
            if (this.isActive) {
                delay(SEARCH_DEBOUNCE_DELAY)
                _searchScreenState.postValue(SearchScreenState.Loading)
                tracksInteractor.searchTracks(query).collect { pair ->
                    processResult(
                        foundTracks = pair.first, errorCode = pair.second
                    )
                }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorCode: Int?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks)

        }
        when {
            errorCode != null -> _searchScreenState.postValue(SearchScreenState.Error)
            tracks.isEmpty() -> _searchScreenState.postValue(SearchScreenState.Empty)
            else -> _searchScreenState.postValue(SearchScreenState.SearchContent(tracks))
        }
    }

    fun searchQueryChanged(new: SearchTrackQuery? = null) {
        if ((lastSearchQuery.value?.query ?: "") == new?.query) return

        lastSearchQuery.value = new
        if (new != null && new.query.isNotEmpty()) {
            searchDebounce(new)
        } else {
            showHistory()
        }
    }

    fun showHistory() {
        searchJob = viewModelScope.launch(Dispatchers.Default) {
            tracksHistoryInteractor
                .getHistory()
                .first()
                .let { history ->
                    if (history.isNotEmpty()) {
                        _searchScreenState.postValue(SearchScreenState.HistoryContent(history))
                    }
                }
        }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.Default) {
            tracksHistoryInteractor.clearHistory()
            _searchScreenState.postValue(SearchScreenState.Default)
        }
    }

    fun onItemClick(track: Track) {
        viewModelScope.launch(Dispatchers.Default) {
            favoriteTrackInteractor
                .isFavorite(track)
                .collect { track ->
                    tracksHistoryInteractor.addTrack(track)
                    _showTrackTrigger.postValue(track)

                    if (searchScreenState.value is SearchScreenState.HistoryContent) {
                        tracksHistoryInteractor
                            .getHistory()
                            .collect { list ->
                                _searchScreenState.postValue(
                                    SearchScreenState.HistoryContent(list)
                                )
                            }
                    }
                }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}