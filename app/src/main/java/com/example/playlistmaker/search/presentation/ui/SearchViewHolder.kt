package com.example.playlistmaker.search.presentation.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx

class SearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName: TextView = itemView.findViewById(R.id.track_name_tv)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name_tv)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time_tv)
    private val artworkUrl100: ImageView = itemView.findViewById(R.id.artwork_iv)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime

        Glide.with(itemView.context)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.artwork_placeholder)
            .centerCrop()
            .transform(RoundedCorners(dpToPx(2f, itemView.context)))
            .into(artworkUrl100)
    }
}