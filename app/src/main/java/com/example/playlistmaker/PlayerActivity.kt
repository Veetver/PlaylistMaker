package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.utils.dpToPx
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_EXTRA = "TRACK_EXTRA"

        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private var track: Track? = null

    private var playerControl: ImageView? = null
    private var trackTimeProgress: TextView? = null

    private val handler = Handler(Looper.getMainLooper())
    private val runnableTrackTimeProgress = object : Runnable {
        override fun run() {
            when(playerState) {
                STATE_PLAYING -> {
                    trackTimeProgress?.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, 300)
                }
                STATE_PREPARED -> {
                    handler.removeCallbacks(this)
                    trackTimeProgress?.text = getString(R.string.track_time_progress_zero)
                }
                else -> {
                    handler.removeCallbacks(this)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<Toolbar>(R.id.toolbar_player).setNavigationOnClickListener {
            this.finish()
        }
        track = Gson().fromJson(intent.getStringExtra(TRACK_EXTRA), Track::class.java)

        initializeFields(track)
        preparePlayer()

        playerControl = findViewById(R.id.player_control)
        trackTimeProgress = findViewById(R.id.track_time_progress)

        playerControl?.setOnClickListener {
            playbackControl()
            handler.post(runnableTrackTimeProgress)
        }
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
                .placeholder(R.drawable.cover_player_placeholder)
                .centerCrop()
                .transform(RoundedCorners(dpToPx(8f, cover.context)))
                .into(cover)

            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = track.trackTime

            if (track.collectionName.isEmpty()) {
                findViewById<TableRow>(R.id.collection_container).isVisible = false
            } else {
                findViewById<TableRow>(R.id.collection_container).isVisible = true
                collectionName.text = track.collectionName
            }

            releaseDate.text =
                LocalDate.parse(track.releaseDate, DateTimeFormatter.ISO_DATE_TIME).year.toString()
            primaryGenreName.text = track.primaryGenreName
            country.text = track.country
        }
    }

    private fun preparePlayer() {
        mediaPlayer.setDataSource(track?.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerControl?.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerControl?.setImageResource(R.drawable.play)
            playerState = STATE_PREPARED

        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        playerControl?.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playerControl?.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnableTrackTimeProgress)
        mediaPlayer.release()
    }
}