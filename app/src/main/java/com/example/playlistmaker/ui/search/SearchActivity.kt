package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.SearchResult
import com.example.playlistmaker.domain.model.SearchTrackQuery
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.TrackList
import com.example.playlistmaker.ui.player.PlayerActivity
import com.example.playlistmaker.ui.player.PlayerActivity.Companion.TRACK_EXTRA
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val tracksInteractor by lazy { Creator.provideTracksInteractor(applicationContext) }

    private val handler = Handler(Looper.getMainLooper())

    private val toolbar: Toolbar by lazy { findViewById(R.id.toolbar_search) }
    private val clearTextBtn: ImageView by lazy { findViewById(R.id.clear_text_btn) }
    private val searchEditText: EditText by lazy { findViewById(R.id.search_edit_text) }
    private val searchResult: RecyclerView by lazy { findViewById(R.id.search_result) }
    private val searchErrorBtn: Button by lazy { findViewById(R.id.search_retry_btn) }
    private val searchHistoryContainer: LinearLayout by lazy { findViewById(R.id.search_history_container) }
    private val searchHistoryRV: RecyclerView by lazy { findViewById(R.id.search_history) }
    private val searchPlaceholder: ScrollView by lazy { findViewById(R.id.search_placeholder) }
    private val clearHistoryBtn: MaterialButton by lazy { findViewById(R.id.clear_history_btn) }
    private val searchProgress: ProgressBar by lazy { findViewById(R.id.search_progress) }

    private val errLayoutView: ScrollView by lazy { findViewById(R.id.search_placeholder) }
    private val errorImgView: ImageView by lazy { findViewById(R.id.search_placeholder_img) }
    private val errorTxtView: TextView by lazy { findViewById(R.id.search_placeholder_text) }
    private val errorBtnView: Button by lazy { findViewById(R.id.search_retry_btn) }

    private val tracksHistory = mutableListOf<Track>()
    private val tracksList = mutableListOf<Track>()

    private var adapter: SearchAdapter = SearchAdapter(tracksList)
    private var searchHistoryAdapter: SearchAdapter = SearchAdapter(tracksHistory)

    private val searchRunnable = Runnable {
        setProgressVisibility(true)
        val searchTrackQuery = SearchTrackQuery(searchEditText.text.toString())
        tracksInteractor.searchTracks(searchTrackQuery) { foundTracks ->
            handler.post {
                this.tracksList.clear()
                this.tracksList.addAll(foundTracks.trackList.list)

                adapter.notifyDataSetChanged()

                setProgressVisibility(false)
                visibilityControl(foundTracks)
            }
        }
    }

    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText.doAfterTextChanged {
            clearTextBtn.isVisible = !searchEditText.text.isNullOrEmpty()
        }

        searchEditText.doOnTextChanged { text, start, before, count ->
            searchHistoryContainer.isVisible =
                searchEditText.hasFocus() && text.isNullOrEmpty() && tracksHistory.isNotEmpty()
            if (!text.isNullOrEmpty()) searchDebounce()
        }

        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryContainer.isVisible =
                hasFocus && searchEditText.text.isEmpty() && tracksHistory.isNotEmpty()
        }

        searchErrorBtn.setOnClickListener {
            handler.post(searchRunnable)
        }

        clearTextBtn.setOnClickListener {
            searchEditText.text = null

            this.tracksList.clear()
            adapter.notifyDataSetChanged()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)

            searchPlaceholder.isVisible = false

            handler.removeCallbacks(searchRunnable)
        }

        clearHistoryBtn.setOnClickListener {
            tracksInteractor.clearTrackHistory()
            tracksHistory.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryContainer.isVisible = false
        }

        adapter.setOnItemClickListener { pos, track ->
            if (!clickDebounce()) return@setOnItemClickListener

            tracksHistory.add(0, track)
            searchHistoryAdapter.notifyItemInserted(0)

            if (tracksHistory.size > 10) {
                tracksHistory.removeLast()
                searchHistoryAdapter.notifyItemRemoved(10)
            }

            searchHistoryAdapter.notifyItemRangeChanged(0, 10)

            tracksInteractor.saveTrackHistory(TrackList(tracksHistory))

            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_EXTRA, Gson().toJson(track))
            startActivity(intent)
        }

        searchHistoryAdapter.setOnItemClickListener { pos, track ->
            if (!clickDebounce()) return@setOnItemClickListener

            tracksHistory.removeAt(pos)
            tracksHistory.add(0, track)
            tracksInteractor.saveTrackHistory(TrackList(tracksHistory))

            searchHistoryAdapter.notifyItemMoved(pos, 0)
            searchHistoryAdapter.notifyItemRangeChanged(0, pos + 1)
            searchHistoryRV.layoutManager?.scrollToPosition(0)

            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_EXTRA, Gson().toJson(track))
            startActivity(intent)
        }

        toolbar.setNavigationOnClickListener {
            this.finish()
        }

        searchResult.adapter = adapter
        searchHistoryRV.adapter = searchHistoryAdapter

        tracksInteractor.getTracksHistory { trackList ->
            tracksHistory.addAll(trackList.list)
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        searchProgress.isVisible = isVisible
        errLayoutView.isVisible = !isVisible
        searchResult.isVisible = !isVisible
    }

    private fun visibilityControl(searchResult: SearchResult) {
        when {
            !searchResult.success -> showError()
            searchResult.trackList.list.isEmpty() -> showEmpty()
            else -> showResult()
        }
    }

    private fun showError() {
        errLayoutView.isVisible = true
        errorImgView.setImageResource(R.drawable.error_search_placeholder)
        errorTxtView.text = getString(R.string.search_error)
        errorBtnView.isVisible = true
        searchResult.isVisible = false
    }

    private fun showEmpty() {
        errLayoutView.isVisible = true
        errorImgView.setImageResource(R.drawable.empty_search_placeholder)
        errorTxtView.text = getString(R.string.search_empty)
        errorBtnView.isVisible = false
        searchResult.isVisible = false
    }

    private fun showResult() {
        errLayoutView.isVisible = false
        searchResult.isVisible = true
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }
}