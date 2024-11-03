package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.domain.api.TrackHistoryInteractor
import com.example.playlistmaker.search.domain.api.TracksInteractor
import com.example.playlistmaker.search.domain.impl.TrackHistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val searchDomainModule = module {
    factory<TrackHistoryInteractor> {
        TrackHistoryInteractorImpl(local = get())
    }

    factory<TracksInteractor> {
        TracksInteractorImpl(repository = get())
    }
}