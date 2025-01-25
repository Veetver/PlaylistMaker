package com.example.playlistmaker.library_new_playlist.di

import com.example.playlistmaker.library_new_playlist.data.LocalDataStorage
import com.example.playlistmaker.library_new_playlist.data.local.ExternalPrivateStorage
import org.koin.dsl.module

val newPlaylistDataModule = module {
    single<LocalDataStorage> {
        ExternalPrivateStorage(
            context = get(),
        )
    }
}