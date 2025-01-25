package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.core.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("DELETE FROM favorite_track WHERE track_id = :trackId")
    suspend fun deleteTrack(trackId: Long)

    @Query("SELECT * FROM favorite_track ORDER BY id DESC")
    fun getTracks(): Flow<List<TrackEntity>>

    @Query("SELECT track_id FROM favorite_track")
    fun getTrackIds(): Flow<List<Long>>
}