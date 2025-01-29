package com.example.playlistmaker.playlist_details.presentation.ui

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.core.data.mappers.PlaylistDetailsMapper.toPlaylistDetailsUI
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.playlist_details.presentation.model.PlaylistDetailsUI
import com.example.playlistmaker.playlist_details.presentation.viewmodel.PlaylistDetailsViewModel
import com.example.playlistmaker.search.presentation.ui.TrackListAdapter
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf


class PlaylistDetailsFragment : Fragment() {
    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private val args: PlaylistDetailsFragmentArgs by navArgs()
    private val viewModel: PlaylistDetailsViewModel by viewModel {
        parametersOf(args.playlist)
    }

    private val adapter = TrackListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playlistsDetailsScreenState.collect { state ->
                    if (!state.isLoading && state.playlist != null) {
                        render(state.playlist.toPlaylistDetailsUI())
                        adapter.setTrackList(state.playlist.trackList)
                    }
                }
            }
        }

        updateBottomSheetPeekHeight()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        adapter.setOnItemClickListener { track ->
            val direction =
                PlaylistDetailsFragmentDirections
                    .actionPlaylistDetailsFragmentToPlayerFragment(track)
            findNavController().navigate(direction)
        }

        val confirmDialog =
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.remove_track_warning))
                .setMessage(getString(R.string.remove_track_warning_body))
                .setNegativeButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }

        adapter.setOnItemLongClickListener { track ->
            confirmDialog
                .setPositiveButton(getString(R.string.remove)) { _, _ ->
                    viewModel.removeTrackFromPlaylist(track)
                }
                .show()
            true
        }

        binding.trackListRv.adapter = adapter

        binding.shareIv.setOnClickListener {
            viewModel.sharePlaylist()
        }

        binding.shareTv.setOnClickListener {
            viewModel.sharePlaylist()
        }

        val bottomSheetMenu = BottomSheetBehavior.from(binding.bottomSheetMenuContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetMenu.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.isVisible = false
                    else -> binding.overlay.isVisible = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.overlay.alpha = (slideOffset + 1) / 2
            }
        })

        binding.menuIv.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        val removePlaylistDialog =
            MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.remove_playlist_warning))
                .setMessage(getString(R.string.remove_playlist_warning_body))
                .setNegativeButton(getString(R.string.no)) { dialog, _ -> dialog.cancel() }
                .setPositiveButton(getString(R.string.yes)) { _, _ ->
                    viewModel.removePlaylist()
                    findNavController().navigateUp()
                }

        binding.deletePlaylistTv.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            removePlaylistDialog.show()
        }
    }

    private fun updateBottomSheetPeekHeight() {
        binding.shareIv.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.shareIv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val behavior = BottomSheetBehavior.from(binding.bottomSheetContainer)

                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                val height = displayMetrics.heightPixels

                var peekHeight = height - binding.shareIv.y - binding.shareIv.height - dpToPx(
                    24f, requireContext()
                )

                if (args.playlist.description.isNullOrEmpty()) {
                    peekHeight += binding.descriptionTv.height
                }

                behavior.peekHeight = peekHeight.toInt()
            }
        })
    }

    private fun render(playlist: PlaylistDetailsUI) {
        binding.nameTv.text = playlist.name
        binding.collectionNameMenuTv.text = playlist.name

        if (playlist.description.isNullOrEmpty()) {
            binding.descriptionTv.isVisible = false
        } else {
            binding.descriptionTv.isVisible = true
            binding.descriptionTv.text = playlist.description
        }

        val playlistTracksCount = requireContext().resources.getQuantityString(
            R.plurals.tracks, playlist.trackCount, playlist.trackCount
        )
        binding.collectionTrackCountTv.text = playlistTracksCount
        binding.collectionTrackCountMenuTv.text = playlistTracksCount

        binding.totalTrackTimeTv.text = requireContext().resources.getQuantityString(
            R.plurals.minutes, playlist.totalTrackTime, playlist.totalTrackTime
        )

        playlist.cover?.let {
            val glide = Glide.with(binding.coverIv).load(playlist.cover)
                .error(R.drawable.cover_player_placeholder)
                .placeholder(R.drawable.cover_player_placeholder)
                .transform(
                    CenterCrop()
                )

            glide.into(binding.coverIv)
            glide
                .transform(
                    CenterCrop(),
                    RoundedCorners(dpToPx(2f, requireContext())),
                )
                .into(binding.coverMenuIv)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}