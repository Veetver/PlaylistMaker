package com.example.playlistmaker.library.presentation.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.library.presentation.model.CreatedPlaylist
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx

class PlaylistAdapter : RecyclerView.Adapter<PlaylistViewHolder>() {

    private var createdPlaylists: List<CreatedPlaylist> = emptyList()
    private var onItemClickListener: (CreatedPlaylist) -> Unit = { }

    fun setTrackList(list: List<CreatedPlaylist>) {
        createdPlaylists = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: (CreatedPlaylist) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return when (viewType) {
            R.layout.item_recyclerview_playlist -> PlaylistViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recyclerview_playlist, parent, false)
            )

            else -> PlaylistViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_recyclerview_playlist_horizontal, parent, false)
            )
        }
    }

    override fun getItemViewType(position: Int): Int = createdPlaylists[position].layout

    override fun getItemCount(): Int = createdPlaylists.size

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(createdPlaylists[position])
        holder.itemView.setOnClickListener {
            onItemClickListener(createdPlaylists[position])
        }
    }
}

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cover: ImageView = itemView.findViewById(R.id.cover_iv)
    private val name: TextView = itemView.findViewById(R.id.collection_name_tv)
    private val trackCount: TextView = itemView.findViewById(R.id.collection_track_count_tv)

    fun bind(model: CreatedPlaylist) {
        name.text = model.name
        trackCount.text =
            itemView.context.resources.getQuantityString(
                R.plurals.tracks,
                model.trackCount,
                model.trackCount
            )

        model.cover?.let {
            Glide.with(itemView.context)
                .load(model.cover)
                .error(R.drawable.cover_player_placeholder)
                .placeholder(R.drawable.cover_player_placeholder)
                .transform(
                    CenterCrop(),
                    RoundedCorners(dpToPx(8f, itemView.context))
                ).into(cover)
        }
    }
}