package com.example.playlistmaker.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.core.data.db.dao.FavoriteTrackDao
import com.example.playlistmaker.core.data.db.dao.PlaylistDao
import com.example.playlistmaker.core.data.db.dao.TrackHistoryDao
import com.example.playlistmaker.core.data.db.entity.PlaylistAndTrackEntity
import com.example.playlistmaker.core.data.db.entity.PlaylistEntity
import com.example.playlistmaker.core.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.core.data.db.entity.TrackEntity
import com.example.playlistmaker.core.data.db.entity.TrackHistoryEntity

@Database(version = 1, entities = [TrackEntity::class, TrackHistoryEntity::class, PlaylistEntity::class, PlaylistTrackEntity::class, PlaylistAndTrackEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackFavoriteDao(): FavoriteTrackDao
    abstract fun trackHistoryDao(): TrackHistoryDao
    abstract fun playlistDao(): PlaylistDao
}