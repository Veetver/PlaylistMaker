package com.example.playlistmaker.ui.player

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.Group
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.PlayerState
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.presentation.mapper.DpToPxConverter.dpToPx
import com.example.playlistmaker.presentation.mapper.MillisToStringFormatter.millisToStringFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlayerActivity : AppCompatActivity() {

    private val playerInteractor = Creator.proidePlayerInterator()
    private val gson = Creator.provideGson()

    private var track: Track? = null

    private var playerControl: ImageView? = null
    private var trackTimeProgress: TextView? = null

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        findViewById<Toolbar>(R.id.toolbar_player).setNavigationOnClickListener {
            this.finish()
        }
        playerControl = findViewById(R.id.player_control)
        trackTimeProgress = findViewById(R.id.track_time_progress)

        track = getTrackFromJson(intent)
        initializeFields(track)

        playerControl?.setOnClickListener {
            playerInteractor.playbackControl()
        }

        playerInteractor.preparePlayer(track = track!!) { progress, state ->
            handler.post {
                when (state) {
                    PlayerState.STATE_PLAYING -> playerControl?.setImageResource(R.drawable.pause)
                    PlayerState.STATE_DEFAULT,
                    PlayerState.STATE_PREPARED,
                    PlayerState.STATE_PAUSED -> playerControl?.setImageResource(R.drawable.play)
                }
                trackTimeProgress?.text = millisToStringFormatter(progress)
            }
        }
    }

    private fun getTrackFromJson(intent: Intent): Track {
        return gson.fromJson(intent.getStringExtra(TRACK_EXTRA), Track::class.java)
    }

    private fun initializeFields(track: Track?) {
        val cover = findViewById<ImageView>(R.id.cover_placeholder)
        val trackName = findViewById<TextView>(R.id.track_name)
        val artistName = findViewById<TextView>(R.id.artist_name)
        val trackTime = findViewById<TextView>(R.id.track_time)
        val collectionName = findViewById<TextView>(R.id.collection_name)
        val releaseDate = findViewById<TextView>(R.id.release_date)
        val primaryGenreName = findViewById<TextView>(R.id.primary_genre_name)
        val country = findViewById<TextView>(R.id.country)

        if (track != null) {
            Glide.with(cover.context)
                .load(track.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.cover_player_placeholder).centerCrop()
                .transform(RoundedCorners(dpToPx(8f, cover.context))).into(cover)

            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTime

            if (track.collectionName.isEmpty()) {
                setCollectionVisibility(false)
            } else {
                setCollectionVisibility(true)
                collectionName.text = track.collectionName
            }

            releaseDate.text =
                LocalDate.parse(track.releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
        }
    }

    private fun setCollectionVisibility(isVisible: Boolean) {
        findViewById<Group>(R.id.collection_group).isVisible = isVisible
    }

    override fun onPause() {
        super.onPause()
        playerInteractor.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerInteractor.stop()
    }

    companion object {
        const val TRACK_EXTRA = "TRACK_EXTRA"
    }
}