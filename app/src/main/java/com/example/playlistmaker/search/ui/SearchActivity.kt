package com.example.playlistmaker.search.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.player.ui.PlayerActivity
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.presentation.state.SearchScreenState
import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var binding: ActivitySearchBinding

    private var searchAdapter: SearchAdapter = SearchAdapter()
    private var searchHistoryAdapter: SearchAdapter = SearchAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ) { true },
        )

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewModel.searchScreenState.observe(this) { state ->
            render(state)
        }

        binding.searchEditText.doAfterTextChanged {
            binding.clearTextBtn.isVisible = !binding.searchEditText.text.isNullOrEmpty()
        }

        binding.searchEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.searchQueryChanged(
                SearchTrackQuery(text.toString())
            )
        }

        binding.searchEditText.setOnFocusChangeListener { _, _ ->
            viewModel.searchQueryChanged()
        }

        binding.searchRetryBtn.setOnClickListener {
            viewModel.retrySearch()
        }

        binding.clearTextBtn.setOnClickListener {
            binding.searchEditText.text = null

            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        }

        binding.clearHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
        }

        viewModel.showTrackTrigger.observe(this) { trackJson ->
            openTrack(trackJson)
        }

        searchAdapter.setOnItemClickListener { _, track -> viewModel.onItemClick(track) }
        searchHistoryAdapter.setOnItemClickListener { _, track -> viewModel.onItemClick(track) }

        binding.toolbarSearch.setNavigationOnClickListener {
            this.finish()
        }

        binding.searchResult.adapter = searchAdapter
        binding.searchHistory.adapter = searchHistoryAdapter
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            SearchScreenState.Default -> showDefault()
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.SearchContent -> {
                searchAdapter.setTrackList(state.trackList)
                showResult()
            }

            is SearchScreenState.HistoryContent -> {
                searchHistoryAdapter.setTrackList(state.trackList)
                showHistory()
            }

            is SearchScreenState.Empty -> showEmpty()
            is SearchScreenState.Error -> showError()
        }
    }

    private fun showDefault() {
        binding.searchProgress.isVisible = false
        binding.searchPlaceholder.isVisible = false
        binding.searchHistoryContainer.isVisible = false
        binding.searchResult.isVisible = false
    }

    private fun showLoading() {
        binding.searchProgress.isVisible = true
        binding.searchPlaceholder.isVisible = false
        binding.searchHistoryContainer.isVisible = false
        binding.searchResult.isVisible = false
    }

    private fun showResult() {
        binding.searchProgress.isVisible = false
        binding.searchHistoryContainer.isVisible = false
        binding.searchPlaceholder.isVisible = false
        binding.searchResult.isVisible = true
    }

    private fun showHistory() {
        binding.searchProgress.isVisible = false
        binding.searchHistoryContainer.isVisible = true
        binding.searchPlaceholder.isVisible = false
        binding.searchResult.isVisible = false
    }

    private fun showEmpty() {
        binding.searchProgress.isVisible = false
        binding.searchPlaceholder.isVisible = true
        binding.searchPlaceholderImg.setImageResource(R.drawable.empty_search_placeholder)
        binding.searchPlaceholderText.text = getString(R.string.search_empty)
        binding.searchRetryBtn.isVisible = false
        binding.searchResult.isVisible = false
        binding.searchHistoryContainer.isVisible = false
    }

    private fun showError() {
        binding.searchProgress.isVisible = false
        binding.searchPlaceholder.isVisible = true
        binding.searchPlaceholderImg.setImageResource(R.drawable.error_search_placeholder)
        binding.searchPlaceholderText.text = getString(R.string.search_error)
        binding.searchRetryBtn.isVisible = true
        binding.searchResult.isVisible = false
        binding.searchHistoryContainer.isVisible = false
    }

    private fun openTrack(trackJson: String) {
        PlayerActivity.show(this, trackJson)
    }
}