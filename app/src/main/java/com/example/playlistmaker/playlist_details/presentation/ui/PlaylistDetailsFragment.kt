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
import com.example.playlistmaker.R
import com.example.playlistmaker.core.data.mappers.PlaylistDetailsMapper.toPlaylistDetailsUI
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.playlist_details.presentation.model.PlaylistDetailsUI
import com.example.playlistmaker.playlist_details.presentation.viewmodel.PlaylistDetailsViewModel
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
                    }
                }
            }
        }

        updateBottomSheetPeekHeight()

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun updateBottomSheetPeekHeight() {
        binding.shareIv.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.shareIv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val behavior = BottomSheetBehavior.from(binding.bottomSheetContainer);

                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                val height = displayMetrics.heightPixels

                var peekHeight = height - binding.shareIv.y - binding.shareIv.height - dpToPx(
                    24f,
                    requireContext()
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
        if (playlist.description.isNullOrEmpty()) {
            binding.descriptionTv.isVisible = false
        } else {
            binding.descriptionTv.isVisible = true
            binding.descriptionTv.text = playlist.description
        }
        binding.collectionTrackCountTv.text = binding.root.context.resources.getQuantityString(
            R.plurals.tracks, playlist.trackCount, playlist.trackCount
        )
        binding.totalTrackTimeTv.text = binding.root.context.resources.getQuantityString(
            R.plurals.minutes, playlist.totalTrackTime, playlist.totalTrackTime
        )

        Glide.with(binding.coverIv).load(playlist.cover)
            .placeholder(R.drawable.cover_player_placeholder).transform(
                CenterCrop()
            ).into(binding.coverIv)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}