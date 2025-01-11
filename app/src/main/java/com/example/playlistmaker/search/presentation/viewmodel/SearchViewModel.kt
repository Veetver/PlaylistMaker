package com.example.playlistmaker.search.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList
import com.example.playlistmaker.search.presentation.state.SearchScreenState
import com.example.playlistmaker.util.SingleLiveEvent
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val tracksHistoryInteractor: TrackHistoryInteractor,
    private val gson: Gson,
) : ViewModel() {

    private val _searchScreenState: MutableLiveData<SearchScreenState> =
        MutableLiveData(SearchScreenState.Default)
    val searchScreenState: LiveData<SearchScreenState> = _searchScreenState

    private val _showTrackTrigger = SingleLiveEvent<String>()
    val showTrackTrigger: LiveData<String> = _showTrackTrigger

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
                    processResult(pair.first, pair.second)
                }
            }
        }
    }

    private fun processResult(foundTracks: TrackList?, errorCode: Int?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.addAll(foundTracks.list)
        }
        when {
            errorCode != null -> _searchScreenState.postValue(SearchScreenState.Error)
            tracks.isEmpty() -> _searchScreenState.postValue(SearchScreenState.Empty)
            else -> _searchScreenState.postValue(SearchScreenState.SearchContent(TrackList(tracks)))
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
        val historyList = tracksHistoryInteractor.getHistory()
        if (historyList.list.isNotEmpty()) {
            _searchScreenState.value = SearchScreenState.HistoryContent(historyList)
        }
    }

    fun clearHistory() {
        tracksHistoryInteractor.clearHistory()
        _searchScreenState.postValue(SearchScreenState.Default)
    }

    fun onItemClick(track: Track) {
        tracksHistoryInteractor.addTrack(track)
        _showTrackTrigger.postValue(gson.toJson(track))

        if (searchScreenState.value is SearchScreenState.HistoryContent) {
            _searchScreenState.postValue(SearchScreenState.HistoryContent(tracksHistoryInteractor.getHistory()))
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}