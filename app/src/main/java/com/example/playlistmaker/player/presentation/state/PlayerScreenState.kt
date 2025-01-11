package com.example.playlistmaker.player.presentation.state

import com.example.playlistmaker.R
import com.example.playlistmaker.player.presentation.model.TrackUI
import java.util.Objects

sealed class PlayerScreenState(val progress: Int = 0, val iconRes: Int = R.drawable.play) {
    class Initializing(val track: TrackUI) : PlayerScreenState()
    class Waiting(progress: Int, iconRes: Int = R.drawable.play) : PlayerScreenState(progress, iconRes)
    class Playing(progress: Int, iconRes: Int = R.drawable.pause) : PlayerScreenState(progress, iconRes)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayerScreenState) return false
        if (other::class != this::class) return false

        return this.progress == other.progress
    }

    override fun hashCode(): Int {
        return Objects.hash(progress, this::class)
    }
}