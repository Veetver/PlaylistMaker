package com.example.playlistmaker.core.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.playlistmaker.core.data.db.entity.PlaylistAndTrackEntity
import com.example.playlistmaker.core.data.db.entity.PlaylistEntity
import com.example.playlistmaker.core.data.db.entity.PlaylistTrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(track: PlaylistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToPlaylist(playlistAndTrackEntity: PlaylistAndTrackEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlaylistTrack(playlistTrackEntity: PlaylistTrackEntity)

    @Query("SELECT * FROM playlist ORDER BY id DESC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT COUNT(playlist_track_id) FROM playlist_and_track WHERE playlist_id = :playlistId")
    fun getTrackCount(playlistId: Long): Flow<Int>

    /**
     * @return PlaylistTrackEntity if exists otherwise null
     */
    @Query("SELECT * FROM playlist_and_track INNER JOIN playlist_track ON playlist_and_track.playlist_track_id = playlist_track.track_id WHERE playlist_id = :playlistId AND playlist_and_track.playlist_track_id = :trackId")
    fun containsInPlaylist(playlistId: Long, trackId: Long): Flow<PlaylistTrackEntity?>

    @Query("SELECT * FROM playlist_and_track INNER JOIN playlist_track ON playlist_and_track.playlist_track_id = playlist_track.track_id WHERE playlist_id = :playlistId")
    fun getPlaylistTracks(playlistId: Long): Flow<List<PlaylistTrackEntity>>

    @Query("SELECT * FROM playlist WHERE id = :playlistId")
    fun getPlaylist(playlistId: Long): Flow<PlaylistEntity?>

    @Query("DELETE FROM playlist_and_track WHERE playlist_id =:playlistId AND playlist_track_id =:trackId")
    fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)

    @Query("DELETE FROM playlist_track WHERE track_id NOT IN (SELECT playlist_track_id FROM playlist_and_track)")
    fun removeRedundantPlaylistTracks()

    @Transaction
    fun removeTrackFromPlaylistAndClear(playlistId: Long, trackId: Long) {
        removeTrackFromPlaylist(playlistId, trackId)
        removeRedundantPlaylistTracks()
    }

    @Query("DELETE FROM playlist WHERE id =:playlistId")
    suspend fun removePlaylist(playlistId: Long)

    @Query("DELETE FROM playlist_and_track WHERE playlist_id =:playlistId")
    suspend fun removePlaylistAndTrack(playlistId: Long)

    @Transaction
    suspend fun removePlaylistAndClear(playlistId: Long) {
        removePlaylist(playlistId)
        removePlaylistAndTrack(playlistId)
        removeRedundantPlaylistTracks()
    }
}