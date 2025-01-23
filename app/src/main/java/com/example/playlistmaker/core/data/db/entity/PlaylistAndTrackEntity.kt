package com.example.playlistmaker.core.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "playlist_and_track",
    primaryKeys = ["playlist_id", "playlist_track_id"]
)
data class PlaylistAndTrackEntity (
    @ColumnInfo(name = "playlist_id")
    val playlistId: Long,
    @ColumnInfo(name = "playlist_track_id")
    val playlistTrackId: Long,
)