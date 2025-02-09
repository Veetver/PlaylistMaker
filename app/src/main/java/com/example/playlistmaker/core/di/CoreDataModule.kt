package com.example.playlistmaker.core.di

import androidx.room.Room
import com.example.playlistmaker.core.data.StringResolverImpl
import com.example.playlistmaker.core.data.db.AppDatabase
import com.example.playlistmaker.core.domain.api.StringResolver
import org.koin.dsl.module

val coreDataModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            klass = AppDatabase::class.java,
            name = "playlistmaker.db"
        ).build()
    }

    single<StringResolver> {
        StringResolverImpl(
            context = get()
        )
    }
}