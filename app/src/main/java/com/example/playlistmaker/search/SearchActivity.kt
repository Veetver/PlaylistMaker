package com.example.playlistmaker.search

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
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.App
import com.example.playlistmaker.PlayerActivity
import com.example.playlistmaker.PlayerActivity.Companion.TRACK_EXTRA
import com.example.playlistmaker.R
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.repository.TracksRepository
import com.example.playlistmaker.repository.impl.ITunesApiDataSource
import com.example.playlistmaker.search.rv.SearchAdapter
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson


class SearchActivity : AppCompatActivity() {

    companion object {
        private const val SEARCH_TEXT = "SEARCH_TEXT"
        private const val SEARCH_RESULT_ERROR = "SEARCH_RESULT_ERROR"
        private const val SEARCH_RESULT = "SEARCH_RESULT"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true

    private val tracksRepository = TracksRepository(ITunesApiDataSource())

    private var searchText = ""
    private var searchResultError = false
    private var tracks = ArrayList<Track>()
    private var tracksHistory = ArrayList<Track>()

    private var adapter: SearchAdapter = SearchAdapter(tracks)
    private var searchHistoryAdapter: SearchAdapter = SearchAdapter(tracksHistory)
    
    private var searchEditText: EditText? = null
    private var searchProgress: ProgressBar? = null
    private var searchResult: RecyclerView? = null

    private val searchRunnable = Runnable {
        setProgressVisibility(true)
        trackSearch(searchEditText?.text.toString())
    }

    private var handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_search)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar_search)
        val clearTextBtn = findViewById<ImageView>(R.id.clear_text_btn)
        searchEditText = findViewById(R.id.search_edit_text)
        searchResult = findViewById(R.id.search_result)
        val searchErrorBtn = findViewById<Button>(R.id.search_retry_btn)
        val searchHistoryContainer = findViewById<LinearLayout>(R.id.search_history_container)
        val searchHistoryRV = findViewById<RecyclerView>(R.id.search_history)
        val searchPlaceholder = findViewById<ScrollView>(R.id.search_placeholder)
        val clearHistoryBtn = findViewById<MaterialButton>(R.id.clear_history_btn)
        searchProgress = findViewById(R.id.search_progress)

        val prefs = (applicationContext as App).getSharedPrefs()

        val searchHistory = SearchHistory(prefs)

        tracksHistory.clear()
        tracksHistory.addAll(searchHistory.getSearchHistory())

        adapter.setOnItemClickListener { pos, track ->
            if (!clickDebounce()) return@setOnItemClickListener

            tracksHistory.add(0, track)
            searchHistoryAdapter.notifyItemInserted(0)

            if (tracksHistory.size > 10) {
                tracksHistory.removeLast()
                searchHistoryAdapter.notifyItemRemoved(10)
            }

            searchHistoryAdapter.notifyItemRangeChanged(0, 10)

            searchHistory.saveHistory(tracksHistory)

            val intent = Intent(this, PlayerActivity::class.java)
            intent.putExtra(TRACK_EXTRA, Gson().toJson(track))
            startActivity(intent)
        }

        searchHistoryAdapter.setOnItemClickListener { pos, track ->
            if (!clickDebounce()) return@setOnItemClickListener

            tracksHistory.removeAt(pos)
            tracksHistory.add(0, track)
            searchHistory.saveHistory(tracksHistory)

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

        clearTextBtn.setOnClickListener {
            searchEditText?.text = null

            this.tracks.clear()
            adapter.notifyDataSetChanged()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText?.windowToken, 0)

            searchPlaceholder.isVisible = false
        }

        searchEditText?.doAfterTextChanged {
            clearTextBtn.isVisible = !searchEditText?.text.isNullOrEmpty()
            searchText = searchEditText?.text.toString()
        }

        searchEditText?.doOnTextChanged { text, start, before, count ->
            searchHistoryContainer.isVisible =
                searchEditText?.hasFocus() == true && text?.isEmpty() == true && tracksHistory.isNotEmpty()
            searchDebounce()
        }

        searchEditText?.setOnFocusChangeListener { _, hasFocus ->
            searchHistoryContainer.isVisible =
                hasFocus && searchEditText?.text?.isEmpty() == true && tracksHistory.isNotEmpty()
        }

        searchErrorBtn.setOnClickListener {
            trackSearch(searchEditText?.text.toString())
        }

        clearHistoryBtn.setOnClickListener {
            searchHistory.clear()
            tracksHistory.clear()
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryContainer.isVisible = false
        }

        searchResult?.adapter = adapter
        searchHistoryRV.adapter = searchHistoryAdapter
    }

    private fun trackSearch(query: String) {
        if (query.isNotEmpty()) {
            tracksRepository.search(
                text = query,
                onSuccess = ::onSearchSuccess,
                onFailure = ::onSearchFailure
            )
        } else {
            this.tracks.clear()
            adapter.notifyDataSetChanged()
        }
    }

    private fun onSearchSuccess(tracks: List<Track>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
        adapter.notifyDataSetChanged()

        searchResultError = false
        showSearchResult()

        setProgressVisibility(false)
    }

    private fun onSearchFailure() {
        this.tracks.clear()
        adapter.notifyDataSetChanged()

        searchResultError = true
        showSearchResult()

        setProgressVisibility(false)
    }

    private fun showSearchResult() {
        val errLayoutView = findViewById<ScrollView>(R.id.search_placeholder)
        val errorImgView = findViewById<ImageView>(R.id.search_placeholder_img)
        val errorTxtView = findViewById<TextView>(R.id.search_placeholder_text)
        val errorBtnView = findViewById<Button>(R.id.search_retry_btn)
        val searchResult = findViewById<RecyclerView>(R.id.search_result)

        if (searchResultError) {
            errLayoutView.isVisible = true
            errorImgView.setImageResource(R.drawable.error_search_placeholder)
            errorTxtView.text = getString(R.string.search_error)
            errorBtnView.isVisible = true
            searchResult.isVisible = false
        } else if (this.tracks.isEmpty()) {
            errLayoutView.isVisible = true
            errorImgView.setImageResource(R.drawable.empty_search_placeholder)
            errorTxtView.text = getString(R.string.search_empty)
            errorBtnView.isVisible = false
            searchResult.isVisible = false
        } else {
            errLayoutView.isVisible = false
            searchResult.isVisible = true
        }
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun setProgressVisibility(isVisible: Boolean) {
        searchProgress?.isVisible = isVisible
        searchResult?.isVisible = !isVisible
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchText)

        // Временно, до бд
        outState.putBoolean(SEARCH_RESULT_ERROR, searchResultError)
        outState.putSerializable(SEARCH_RESULT, this.tracks)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        findViewById<EditText>(R.id.search_edit_text).setText(
            savedInstanceState.getString(SEARCH_TEXT) ?: ""
        )
        this.searchResultError = savedInstanceState.getBoolean(SEARCH_RESULT_ERROR)

        // Временно, до бд
        this.tracks.clear()
        this.tracks.addAll(savedInstanceState.getSerializable(SEARCH_RESULT) as ArrayList<Track>)
        showSearchResult()
    }
}