package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.player.presentation.model.TrackUI
import java.util.Objects

sealed class PlayerScreenState(val track: TrackUI, val progress: Int = 0) {
    class Initializing(track: TrackUI) : PlayerScreenState(track) {
        override fun copy(track: TrackUI, progress: Int): PlayerScreenState = Initializing(track)
    }

    class Waiting(track: TrackUI, progress: Int) : PlayerScreenState(track, progress) {
        override fun copy(track: TrackUI, progress: Int): PlayerScreenState = Waiting(track, progress)
    }
    class Playing(track: TrackUI, progress: Int) : PlayerScreenState(track, progress) {
        override fun copy(track: TrackUI, progress: Int): PlayerScreenState = Playing(track, progress)
    }

    abstract fun copy(track: TrackUI = this.track, progress: Int = this.progress): PlayerScreenState

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayerScreenState) return false
        if (other::class != this::class) return false

        return this.progress == other.progress && this.track.isFavorite == other.track.isFavorite
    }

    override fun hashCode(): Int {
        return Objects.hash(progress, this::class)
    }
}