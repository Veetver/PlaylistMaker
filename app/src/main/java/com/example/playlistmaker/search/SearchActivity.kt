package com.example.playlistmaker.search

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.repository.TracksRepository
import com.example.playlistmaker.repository.impl.ITunesApiDataSource
import com.example.playlistmaker.search.rv.SearchAdapter


class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_RESULT_ERROR = "SEARCH_RESULT_ERROR"
        const val SEARCH_RESULT = "SEARCH_RESULT"
    }

    private val tracksRepository = TracksRepository(ITunesApiDataSource())

    private var searchText = ""
    private var searchResultError = false
    private var tracks = ArrayList<Track>()

    private val adapter = SearchAdapter(tracks)

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
        val searchEditText = findViewById<EditText>(R.id.search_edit_text)
        val searchResult = findViewById<RecyclerView>(R.id.search_result)
        val searchErrorBtn = findViewById<Button>(R.id.search_retry_btn)

        toolbar.setNavigationOnClickListener {
            this.finish()
        }

        clearTextBtn.setOnClickListener {
            searchEditText.text = null

            this.tracks.clear()
            adapter.notifyDataSetChanged()

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        }

        searchEditText.doAfterTextChanged {
            clearTextBtn.isVisible = !searchEditText.text.isNullOrEmpty()
            searchText = searchEditText.text.toString()
        }

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackSearch(searchEditText.text.toString())
                true
            }
            false
        }

        searchErrorBtn.setOnClickListener {
            trackSearch(searchEditText.text.toString())
        }

        searchResult.adapter = adapter
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
    }

    private fun onSearchFailure() {
        this.tracks.clear()
        adapter.notifyDataSetChanged()

        searchResultError = true
        showSearchResult()
    }

    private fun showSearchResult() {
        val errLayoutView = findViewById<LinearLayout>(R.id.search_placeholder)
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