package com.example.playlistmaker.search.presentation.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.Track

class TrackListAdapter() :
    RecyclerView.Adapter<SearchViewHolder>() {

    private var trackList: List<Track> = emptyList()
    private var onItemClickListener: (Track) -> Unit = { }
    private var onItemLongClickListener: (Track) -> Boolean = { false }

    fun setTrackList(list: List<Track>) {
        trackList = list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: (Track) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: (Track) -> Boolean) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_search_activity, parent, false)
    )


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener(trackList[position])
        }

        holder.itemView.setOnLongClickListener {
            onItemLongClickListener(trackList[position])
        }
    }

    override fun getItemCount() = trackList.size


}