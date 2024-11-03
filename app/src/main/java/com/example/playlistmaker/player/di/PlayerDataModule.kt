package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.player.domain.api.PlayerRepository
import org.koin.dsl.module

val playerDataModule = module {
    factory<MediaPlayer> {
        MediaPlayer()
    }

    factory<PlayerRepository> {
        MediaPlayerRepositoryImpl(
            mediaPlayer = get()
        )
    }
}