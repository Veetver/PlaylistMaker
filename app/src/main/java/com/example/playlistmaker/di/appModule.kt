package com.example.playlistmaker.di

import com.example.playlistmaker.player.di.playerDataModule
import com.example.playlistmaker.player.di.playerDomainModule
import com.example.playlistmaker.player.di.playerPresentationModule
import com.example.playlistmaker.search.di.data.searchDataModule
import com.example.playlistmaker.search.di.searchDomainModule
import com.example.playlistmaker.search.di.searchPresentationModule
import com.example.playlistmaker.settings.di.settingsDataModule
import com.example.playlistmaker.settings.di.settingsDomainModule
import com.example.playlistmaker.settings.di.settingsPresentationModule
import com.example.playlistmaker.share.di.shareDataModule
import com.example.playlistmaker.share.di.shareDomainModule
import com.google.gson.Gson
import org.koin.dsl.module

val appModule = module {
    // PlayerFeature
    includes(playerDataModule, playerDomainModule, playerPresentationModule)

    // SearchFeature
    includes(searchDataModule, searchDomainModule, searchPresentationModule)

    // SettingsFeature
    includes(settingsDataModule, settingsDomainModule, settingsPresentationModule)

    // ShareFeature
    includes(shareDataModule, shareDomainModule)

    single {
        Gson()
    }
}