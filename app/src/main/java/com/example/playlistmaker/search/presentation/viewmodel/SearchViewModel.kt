package com.example.playlistmaker.search.presentation.viewmodel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.state.SearchScreenState
import com.example.playlistmaker.util.SingleLiveEvent
import com.google.gson.Gson

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

    private val lastSearchQuery: MutableLiveData<SearchTrackQuery> = MutableLiveData()

    private val handler = Handler(Looper.getMainLooper())

    private val runnableToken = Any()

    fun retrySearch() {
        val searchQuery = lastSearchQuery.value
        if (searchQuery != null) {
            handler.post(getSearchRunnable(searchQuery))
        }
    }

    fun searchQueryChanged(new: SearchTrackQuery? = null) {
        lastSearchQuery.value = new
        handler.removeCallbacksAndMessages(runnableToken)
        if (new != null && new.query.isNotEmpty()) {
            handler.postDelayed(
                getSearchRunnable(new),
                runnableToken,
                SEARCH_DEBOUNCE_DELAY
            )
        } else {
            showHistory()
        }
    }

    private fun showHistory() {
        val historyList = tracksHistoryInteractor.getHistory()
        if (historyList.list.isNotEmpty()) {
            _searchScreenState.value = SearchScreenState.HistoryContent(historyList)
        }
    }

    fun clearHistory() {
        tracksHistoryInteractor.clearHistory()
        _searchScreenState.postValue(SearchScreenState.Default)
    }

    private fun getSearchRunnable(new: SearchTrackQuery): Runnable {
        return Runnable {
            _searchScreenState.postValue(SearchScreenState.Loading)
            tracksInteractor.searchTracks(new) { searchResult ->
                when {
                    !searchResult.success -> _searchScreenState.postValue(SearchScreenState.Error)
                    searchResult.trackList.list.isEmpty() -> _searchScreenState.postValue(
                        SearchScreenState.Empty
                    )

                    else -> _searchScreenState.postValue(
                        SearchScreenState.SearchContent(
                            searchResult.trackList
                        )
                    )
                }
            }
        }
    }

    fun onItemClick(track: Track) {
        tracksHistoryInteractor.addTrack(track)
        _showTrackTrigger.postValue(gson.toJson(track))
    }


    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val tracksInteractor = Creator.provideTracksInteractor()
                val tracksHistoryInteractor = Creator.provideTrackHistoryInteractor()
                val gson = Creator.provideGson()

                SearchViewModel(
                    tracksInteractor,
                    tracksHistoryInteractor,
                    gson
                )
            }
        }
    }
}