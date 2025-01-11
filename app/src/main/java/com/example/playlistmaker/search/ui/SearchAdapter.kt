package com.example.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.search.domain.model.Track
import com.example.playlistmaker.search.domain.model.TrackList

class SearchAdapter() :
    RecyclerView.Adapter<SearchViewHolder>() {

    private var trackList: List<Track> = emptyList()
    private var onItemClickListener: (Track) -> Unit = { }

    fun setTrackList(list: TrackList) {
        trackList = list.list
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(onItemClickListener: (Track) -> Unit) {
        this.onItemClickListener = onItemClickListener
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
    }

    override fun getItemCount() = trackList.size


}