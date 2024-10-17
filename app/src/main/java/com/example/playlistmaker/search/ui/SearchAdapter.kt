package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.TrackList
import com.example.playlistmaker.search.domain.model.Track

class SearchAdapter() :
    RecyclerView.Adapter<SearchViewHolder>() {

    private var trackList: List<Track> = emptyList()
    private var onItemClickListener: (Int, Track) -> Unit = { i: Int, track: Track -> }

    fun setTrackList(list: TrackList) {
        trackList = list.list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: (Int, Track) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SearchViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_search_activity, parent, false)
    )


    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {
            onItemClickListener(position, trackList[position])
        }
    }

    override fun getItemCount() = trackList.size


}