package com.example.playlistmaker.core.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "favorite_track",
    indices = [Index(value = ["track_id"], unique = true)]
)
data class TrackEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "track_id")
    val trackId: Long,
    @ColumnInfo(name = "name")
    val trackName: String?,
    @ColumnInfo(name = "artist_name")
    val artistName: String?,
    @ColumnInfo(name = "time")
    val trackTime: Long?,
    @ColumnInfo(name = "artwork_url_100")
    val artworkUrl100: String?,
    @ColumnInfo(name = "collection_name")
    val collectionName: String?,
    @ColumnInfo(name = "release_date")
    val releaseDate: String?,
    @ColumnInfo(name = "primary_genre_name")
    val primaryGenreName: String?,
    @ColumnInfo(name = "country")
    val country: String?,
    @ColumnInfo(name = "preview_url")
    val previewUrl: String?,
)