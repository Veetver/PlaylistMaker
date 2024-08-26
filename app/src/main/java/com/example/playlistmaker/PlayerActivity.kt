package com.example.playlistmaker

import android.os.Bundle
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PlayerActivity : AppCompatActivity() {

    companion object {
        const val TRACK_EXTRA = "TRACK_EXTRA"
    }

    private var track: Track? = null

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
                .load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
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
}