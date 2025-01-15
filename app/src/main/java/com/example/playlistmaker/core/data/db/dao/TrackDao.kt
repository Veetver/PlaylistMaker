package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.core.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM favorite_track WHERE track_id = :trackId")
    suspend fun deleteTrack(trackId: Long)

    @Query("SELECT * FROM favorite_track")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT track_id FROM favorite_track")
    suspend fun getTrackIds(): List<Long>
}