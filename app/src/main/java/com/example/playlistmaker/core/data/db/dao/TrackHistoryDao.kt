package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.core.data.db.entity.TrackHistoryEntity

@Dao
interface TrackHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(track: TrackHistoryEntity)

    @Query("DELETE FROM history_track WHERE track_id NOT IN (SELECT track_id from history_track ORDER BY id DESC LIMIT 10 )")
    suspend fun limitTable()

    @Query("DELETE FROM history_track")
    suspend fun dropTable()

    @Query("SELECT * FROM history_track")
    suspend fun getTracks(): List<TrackHistoryEntity>
}