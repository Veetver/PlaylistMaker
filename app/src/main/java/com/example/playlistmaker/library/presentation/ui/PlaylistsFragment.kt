package com.example.playlistmaker.library.presentation.ui

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.FragmentPlaylistsBinding
import com.example.playlistmaker.library.presentation.viewmodel.PlaylistsViewModel
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {
    private lateinit var binding: FragmentPlaylistsBinding
    private val viewModel: PlaylistsViewModel by viewModel()

    private val adapter = PlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createPlaylistBtn.setOnClickListener {
            binding.root
                .findNavController()
                .navigate(LibraryFragmentDirections.actionLibraryFragmentToNewPlaylist())
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel
                .playlistsScreenState
                .collect { state ->
                    if (state.list.isEmpty()) {
                        showEmpty()
                    } else {
                        adapter.setTrackList(state.list)
                        showPlaylists()
                    }
                }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.updatePlaylists()
            }
        }


        binding.playlistRv.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val position = parent.getChildAdapterPosition(view)
                val spanCount = 2
                val spacing = dpToPx(8f, view.context)
                val column = position % spanCount

                outRect.left = (spacing * 2) - column * (spacing + spacing / spanCount)
                outRect.right = (spacing * 2) + (column - 1) * (spacing + spacing / spanCount)
                outRect.top = 0
                outRect.bottom = spacing * 2
            }
        })

        binding.playlistRv.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.playlistRv.adapter = adapter
    }

    private fun showEmpty() {
        binding.playlistRv.isVisible = false
        binding.emptyPlaylistsGroup.isVisible = true
    }

    private fun showPlaylists() {
        binding.playlistRv.isVisible = true
        binding.emptyPlaylistsGroup.isVisible = false
    }

    companion object {
        fun newInstance(): PlaylistsFragment {
            return PlaylistsFragment()
        }
    }
}