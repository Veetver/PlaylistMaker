package com.example.playlistmaker.player.presentation.ui

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
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.core.presentation.ui.utils.playlistmakerSnackbar
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.library.presentation.ui.PlaylistAdapter
import com.example.playlistmaker.player.presentation.mapper.MillisToStringFormatter.millisToStringFormatter
import com.example.playlistmaker.player.presentation.mapper.TrackMapper
import com.example.playlistmaker.player.presentation.model.TrackUI
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerFragment : Fragment() {
    private val viewModel: PlayerViewModel by viewModel()
    {
        parametersOf(args.track)
    }
    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private val args: PlayerFragmentArgs by navArgs()

    private val adapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbarPlayer.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.playerControl.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.imageView.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        initializeFields(track = TrackMapper.toTrackUI(args.track))

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerScreenState.collect { state ->
                    when (state) {
                        is PlayerScreenState.Initializing -> initializeFields(state.track)
                        is PlayerScreenState.Playing, is PlayerScreenState.Waiting -> updateUI(state)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel
                .playerTrackState
                .collect { state ->
                    if (state.inProgress) {
                        if (state.isAdded) {
                            playlistmakerSnackbar(
                                binding.root,
                                requireContext().getString(
                                    R.string.added_to_playlist,
                                    state.playlist?.name
                                )
                            ).show()
                        } else {
                            playlistmakerSnackbar(
                                binding.root,
                                requireContext().getString(
                                    R.string.already_added_to_playlist,
                                    state.playlist?.name
                                )
                            ).show()
                        }
                    }
                }
        }

        binding.playlistRvHorizontal.adapter = adapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .playlistsState.collect { state ->
                        if (!state.isLoading) {
                            adapter.setTrackList(state.list)
                        }
                    }
            }
        }

        adapter.setOnItemClickListener { item ->
            viewModel.addToPlaylist(item)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1) / 2
            }
        })

        binding.addToCollectionImageView.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                viewModel.loadPlaylists()
            }.invokeOnCompletion {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        binding.createPlaylistButton.setOnClickListener {
            findNavController().navigate(R.id.newPlaylist)
        }
    }

    private fun updateUI(state: PlayerScreenState) {
        binding.trackTimeProgress.text = millisToStringFormatter(state.progress)
        binding.playerControl.setImageResource(state.iconRes)
        binding.imageView.setImageResource(
            if (state.track.isFavorite) R.drawable.favorite_active else R.drawable.favorite_inactive
        )
    }

    private fun initializeFields(track: TrackUI) {
        Glide.with(binding.coverPlaceholder.context).load(track.artworkUrl512)
            .placeholder(R.drawable.cover_player_placeholder).centerCrop()
            .transform(RoundedCorners(dpToPx(8f, binding.coverPlaceholder.context)))
            .into(binding.coverPlaceholder)

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = track.trackTime

        if (track.collectionName.isEmpty()) {
            setCollectionVisibility(false)
        } else {
            setCollectionVisibility(true)
            binding.collectionName.text = track.collectionName
        }

        binding.releaseDate.text = track.releaseDate
        binding.primaryGenreName.text = track.primaryGenreName
        binding.country.text = track.country
    }

    private fun setCollectionVisibility(isVisible: Boolean) {
        binding.collectionGroup.isVisible = isVisible
    }

    override fun onPause() {
        super.onPause()
        viewModel.playerPause()
    }
}