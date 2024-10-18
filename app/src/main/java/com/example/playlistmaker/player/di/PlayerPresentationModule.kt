package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.presentation.viewmodel.PlayerViewModel
import com.example.playlistmaker.search.domain.model.Track
import com.google.gson.Gson
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val playerPresentationModule = module {
    viewModel { (jsonTrack: String) ->
        val gson: Gson by inject()
        val track = gson.fromJson(jsonTrack, Track::class.java)
        PlayerViewModel(track, get())
    }
}