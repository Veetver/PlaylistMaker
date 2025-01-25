package com.example.playlistmaker.library_new_playlist.di

import com.example.playlistmaker.library_new_playlist.domain.impl.LoadFileUseCase
import com.example.playlistmaker.library_new_playlist.domain.impl.SaveFileUseCase
import org.koin.dsl.module

val newPlaylistDomainModule = module {
    factory {
        LoadFileUseCase(
            storage = get()
        )
    }

    factory {
        SaveFileUseCase(
            storage = get()
        )
    }

}