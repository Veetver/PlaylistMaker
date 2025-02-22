package com.example.playlistmaker.library.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.library.presentation.state.FavoritesScreenState
import com.example.playlistmaker.library.presentation.viewmodel.FavoritesViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.presentation.ui.TrackListAdapter
import com.example.playlistmaker.util.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private val viewModel: FavoritesViewModel by viewModel()
    private var trackListAdapter: TrackListAdapter = TrackListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateState()
                viewModel.favoritesScreenState.collect { state ->
                        render(state)
                    }
            }
        }

        val itemClickDebounce = debounce<Track>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false,
        ) { track ->
            viewModel.onItemClick(track)
        }

        trackListAdapter.setOnItemClickListener(itemClickDebounce)
        binding.favoritesRv.adapter = trackListAdapter

        viewModel.showTrackTrigger.observe(viewLifecycleOwner) { track ->
                openTrack(track)
            }
    }

    private fun openTrack(track: Track) {
        findNavController().navigate(
            LibraryFragmentDirections.actionLibraryFragmentToPlayerFragment(track)
        )
    }

    private fun render(state: FavoritesScreenState) {
        when (state) {
            is FavoritesScreenState.Content -> {
                trackListAdapter.setTrackList(state.trackList)
                showContent()
            }

            FavoritesScreenState.Empty -> showEmpty()
            FavoritesScreenState.Loading -> showLoading()
        }
    }

    private fun showLoading() {
        binding.placeholderIv.isVisible = false
        binding.placeholderTv.isVisible = false
        binding.favoritesRv.isVisible = false
    }

    private fun showEmpty() {
        binding.placeholderIv.isVisible = true
        binding.placeholderTv.isVisible = true
        binding.favoritesRv.isVisible = false
    }

    private fun showContent() {
        binding.placeholderIv.isVisible = false
        binding.placeholderTv.isVisible = false
        binding.favoritesRv.isVisible = true
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 300L

        fun newInstance(): FavoritesFragment {
            return FavoritesFragment()
        }
    }
}