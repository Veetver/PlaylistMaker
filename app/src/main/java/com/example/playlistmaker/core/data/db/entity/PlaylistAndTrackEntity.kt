package com.example.playlistmaker.core.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist_and_track",
    indices = [Index(value = ["playlist_id", "playlist_track_id"], unique = true)]
)
data class PlaylistAndTrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,
    @ColumnInfo(name = "playlist_track_id")
    val playlistTrackId: Long,
)