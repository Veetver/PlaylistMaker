package com.example.playlistmaker.search.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.models.Track

class SearchAdapter(private val trackList: List<Track>) :
    RecyclerView.Adapter<SearchViewHolder>() {

    private var onItemClickListener: (Int, Track) -> Unit = { i: Int, track: Track -> }

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