package com.example.playlistmaker.library_new_playlist.presentation.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
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
import com.example.playlistmaker.core.presentation.ui.utils.playlistmakerSnackbar
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.library_new_playlist.presentation.state.NewPlaylistState
import com.example.playlistmaker.library_new_playlist.presentation.viewmodel.NewPlaylistViewModel
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : Fragment() {
    private val viewModel: NewPlaylistViewModel by viewModel()

    private var _binding: FragmentNewPlaylistBinding? = null
    private val binding get() = _binding!!
    private val args: NewPlaylistFragmentArgs by navArgs()

    private var isImageAdded: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (args.playlistId != -1L) {
            viewModel.setPlaylist(args.playlistId)

            binding.toolbar.title = "Редактировать"
            binding.createPlaylistBtn.text = "Сохранить"
        }

        val confirmDialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.new_playlist_exit_warning))
            .setMessage(getString(R.string.new_playlist_exit_warning_body))
            .setNeutralButton(getString(R.string.cancel)) { dialog, _ -> dialog.cancel() }
            .setPositiveButton(getString(R.string.confirm)) { _, _ ->
                findNavController().navigateUp()
            }

        binding.toolbar.setNavigationOnClickListener {
            navigateUp(confirmDialog)
        }

        binding.nameEt.doOnTextChanged { text, _, _, _ ->
            viewModel.setName(text.toString())
            binding.createPlaylistBtn.isEnabled = !text.isNullOrEmpty()
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newPlaylistScreenState.collect { state ->
                    if (state.isCreated) {
                        if (args.playlistId == -1L) {
                            playlistmakerSnackbar(
                                binding.root,
                                requireContext().getString(
                                    R.string.create_playlist_success,
                                    state.details.name
                                )
                            ).show()
                        }
                        findNavController().navigateUp()
                    } else {
                        renderUI(state)
                    }
                }
            }
        }

        binding.descriptionEt.doOnTextChanged { text, _, _, _ ->
            viewModel.setDescription(text.toString())
        }


        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.setCover(uri)
                }
            }

        binding.coverIv.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.createPlaylistBtn.setOnClickListener {
            viewModel.upsertPlaylist()
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner) {
                navigateUp(confirmDialog)
            }
    }

    private fun renderUI(state: NewPlaylistState) {
        if (binding.nameEt.text.toString() != state.details.name) {
            binding.nameEt.setText(state.details.name)
        }
        if (binding.descriptionEt.text.toString() != state.details.description) {
            binding.descriptionEt.setText(state.details.description)
        }
        showImage(binding.coverIv, state.details.uri)
    }

    private fun navigateUp(dialog: MaterialAlertDialogBuilder) {
        if (
            (binding.nameEt.text.toString().isNotEmpty() ||
            binding.descriptionEt.text.toString().isNotEmpty() ||
            isImageAdded) && args.playlistId == -1L
        ) {
            dialog.show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showImage(imageView: ImageView, uri: Uri) {
        Glide.with(imageView)
            .load(uri)
            .placeholder(R.drawable.collection_cover_placeholder)
            .transform(
                CenterCrop(),
                RoundedCorners(dpToPx(8f, imageView.context))
            )
            .into(imageView)

        isImageAdded = true
    }

    override fun onDestroyView() {
        _binding = null
        isImageAdded = false
        super.onDestroyView()
    }
}