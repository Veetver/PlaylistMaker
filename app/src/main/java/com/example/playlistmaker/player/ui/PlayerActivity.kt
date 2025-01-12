package com.example.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.player.presentation.mapper.MillisToStringFormatter.millisToStringFormatter
import com.example.playlistmaker.player.presentation.model.TrackUI
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(intent.getStringExtra(TRACK_EXTRA) ?: "")
    }
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayerBinding.inflate(layoutInflater)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbarPlayer.setNavigationOnClickListener {
            this.finish()
        }

        binding.playerControl.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.imageView.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        lifecycle.coroutineScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.playerScreenState.collect { state ->
                    when (state) {
                        is PlayerScreenState.Initializing -> initializeFields(state.track)
                        is PlayerScreenState.Playing, is PlayerScreenState.Waiting -> updateUI(state)
                    }
                }
            }
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

    companion object {
        const val TRACK_EXTRA = "TRACK_EXTRA"

        fun show(context: Context, trackJson: String) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra(TRACK_EXTRA, trackJson)
            context.startActivity(intent)
        }
    }
}