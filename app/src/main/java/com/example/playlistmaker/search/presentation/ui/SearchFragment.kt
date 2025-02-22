package com.example.playlistmaker.search.presentation.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.search.domain.model.SearchTrackQuery
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.state.SearchScreenState
import com.example.playlistmaker.search.presentation.viewmodel.SearchViewModel
import com.example.playlistmaker.util.debounce
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private var trackListAdapter: TrackListAdapter = TrackListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel
            .searchScreenState
            .observe(viewLifecycleOwner) { state ->
                render(state)
            }

        binding.searchEt.doAfterTextChanged {
            binding.clearTextIv.isVisible = !binding.searchEt.text.isNullOrEmpty()
        }

        binding.searchEt.doOnTextChanged { text, _, _, _ ->
            viewModel.searchQueryChanged(SearchTrackQuery(text.toString()))
        }

        binding.searchEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.searchEt.text.isNullOrEmpty()) viewModel.showHistory()
        }

        binding.searchRetryBtn.setOnClickListener {
            viewModel.retrySearch()
        }

        binding.clearTextIv.setOnClickListener {
            binding.searchEt.text = null

            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchEt.windowToken, 0)
        }

        binding.clearHistoryBtn.setOnClickListener {
            viewModel.clearHistory()
        }

        viewModel.showTrackTrigger.observe(viewLifecycleOwner) { track ->
            openTrack(track)
        }

        val searchDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false,
        ) { track ->
            viewModel.onItemClick(track)
        }

        trackListAdapter.setOnItemClickListener(searchDebounce)

        binding.trackListRv.adapter = trackListAdapter
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            SearchScreenState.Default -> showDefault()
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.SearchContent -> {
                trackListAdapter.setTrackList(state.trackList)
                showResult()
            }

            is SearchScreenState.HistoryContent -> {
                trackListAdapter.setTrackList(state.trackList)
                showHistory()
            }

            is SearchScreenState.Empty -> showEmpty()
            is SearchScreenState.Error -> showError()
        }
    }

    private fun showDefault() {
        binding.searchProgress.isVisible = false
        binding.placeholderGroup.isVisible = false
        binding.searchRetryBtn.isVisible = false
        binding.historyGroup.isVisible = false
        binding.trackListRv.isVisible = false
    }

    private fun showLoading() {
        binding.searchProgress.isVisible = true
        binding.placeholderGroup.isVisible = false
        binding.searchRetryBtn.isVisible = false
        binding.historyGroup.isVisible = false
        binding.trackListRv.isVisible = false
    }

    private fun showResult() {
        binding.searchProgress.isVisible = true
        binding.placeholderGroup.isVisible = false
        binding.searchRetryBtn.isVisible = false
        binding.historyGroup.isVisible = false
        binding.trackListRv.isVisible = true
        binding.searchProgress.isVisible = false
    }

    private fun showHistory() {
        binding.searchProgress.isVisible = false
        binding.placeholderGroup.isVisible = false
        binding.searchRetryBtn.isVisible = false
        binding.historyGroup.isVisible = true
        binding.trackListRv.isVisible = true
        binding.searchProgress.isVisible = false
    }

    private fun showEmpty() {
        binding.searchProgress.isVisible = false
        binding.placeholderGroup.isVisible = true
        binding.searchPlaceholderIv.setImageResource(R.drawable.empty_placeholder)
        binding.searchPlaceholderTv.text = getString(R.string.search_empty)
        binding.searchRetryBtn.isVisible = false
        binding.trackListRv.isVisible = false
    }

    private fun showError() {
        binding.searchProgress.isVisible = false
        binding.placeholderGroup.isVisible = true
        binding.searchPlaceholderIv.setImageResource(R.drawable.error_search_placeholder)
        binding.searchPlaceholderTv.text = getString(R.string.search_error)
        binding.searchRetryBtn.isVisible = true
        binding.trackListRv.isVisible = false
    }

    private fun openTrack(track: Track) {
        findNavController().navigate(
            SearchFragmentDirections.actionSearchFragmentToPlayerFragment(track)
        )
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L
    }
}