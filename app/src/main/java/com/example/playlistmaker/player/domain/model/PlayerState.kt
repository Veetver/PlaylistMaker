package com.example.playlistmaker.player.domain.model

import java.util.Objects


sealed class PlayerState(
    val progress: Int
) {
    object Default : PlayerState(
        progress = 0
    )

    object Prepared : PlayerState(
        progress = 0
    )

    class Playing(
        progress: Int
    ) : PlayerState(progress)

    class Paused(
        progress: Int,
    ) : PlayerState(progress)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayerState) return false
        if (other::class != this::class) return false

        return this.progress == other.progress
    }

    override fun hashCode(): Int {
        return Objects.hash(progress, this::class)
    }
}