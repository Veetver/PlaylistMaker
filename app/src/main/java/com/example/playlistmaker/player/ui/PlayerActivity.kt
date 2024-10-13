package com.example.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx
import com.example.playlistmaker.player.presentation.mapper.MillisToStringFormatter.millisToStringFormatter
import com.example.playlistmaker.player.presentation.state.PlayerScreenState
import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlayerActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel
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

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(intent.getStringExtra(TRACK_EXTRA) ?: "")
        ).get(PlayerViewModel::class.java)

        binding.toolbarPlayer.setNavigationOnClickListener {
            this.finish()
        }

        binding.playerControl.setOnClickListener {
            viewModel.playbackControl()
        }

        viewModel.trackLiveData.observe(this) { track ->
            initializeFields(track)
        }

        viewModel.playerScreenStateLiveData.observe(this) { state ->
            changeControlButton(state)
        }

        viewModel.playerProgressLiveData.observe(this) { progress ->
            binding.trackTimeProgress.text = millisToStringFormatter(progress)
        }
    }

    private fun changeControlButton(state: PlayerScreenState) {
        when (state) {
            is PlayerScreenState.Playing -> {
                binding.playerControl.setImageResource(R.drawable.pause)
            }

            is PlayerScreenState.Waiting -> {
                binding.playerControl.setImageResource(R.drawable.play)
            }
        }
    }

    private fun initializeFields(track: Track) {
        Glide.with(binding.coverPlaceholder.context)
            .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
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

        binding.releaseDate.text =
            LocalDate.parse(track.releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
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