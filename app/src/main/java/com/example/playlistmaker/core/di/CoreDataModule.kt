package com.example.playlistmaker.core.di

import androidx.room.Room
import com.example.playlistmaker.core.data.db.AppDatabase
import org.koin.dsl.module

val coreDataModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "playlistmaker.db"
        ).build()
    }
}