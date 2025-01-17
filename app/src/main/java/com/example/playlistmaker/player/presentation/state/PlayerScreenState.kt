package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.R
import com.example.playlistmaker.player.presentation.model.TrackUI
import java.util.Objects

sealed class PlayerScreenState(val track: TrackUI, val progress: Int = 0, val iconRes: Int = R.drawable.play) {
    class Initializing(track: TrackUI) : PlayerScreenState(track) {
        override fun copy(track: TrackUI, progress: Int, iconRes: Int): PlayerScreenState = Initializing(track)
    }

    class Waiting(track: TrackUI, progress: Int, iconRes: Int = R.drawable.play) : PlayerScreenState(track, progress, iconRes) {
        override fun copy(track: TrackUI, progress: Int, iconRes: Int): PlayerScreenState = Waiting(track, progress, iconRes)
    }
    class Playing(track: TrackUI, progress: Int, iconRes: Int = R.drawable.pause) : PlayerScreenState(track, progress, iconRes) {
        override fun copy(track: TrackUI, progress: Int, iconRes: Int): PlayerScreenState = Playing(track, progress, iconRes)
    }

    abstract fun copy(track: TrackUI = this.track, progress: Int = this.progress, iconRes: Int = this.iconRes): PlayerScreenState

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