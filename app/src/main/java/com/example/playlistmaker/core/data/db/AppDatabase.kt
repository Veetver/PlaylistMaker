package com.example.playlistmaker.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.core.data.db.dao.TrackDao
import com.example.playlistmaker.core.data.db.dao.TrackHistoryDao
import com.example.playlistmaker.core.data.db.entity.TrackEntity
import com.example.playlistmaker.core.data.db.entity.TrackHistoryEntity

@Database(version = 1, entities = [TrackEntity::class, TrackHistoryEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackFavoriteDao(): TrackDao
    abstract fun trackHistoryDao(): TrackHistoryDao
}