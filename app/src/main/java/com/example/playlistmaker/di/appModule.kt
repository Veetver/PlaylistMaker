package com.example.playlistmaker.di

import com.example.playlistmaker.core.di.coreDataModule
import com.example.playlistmaker.core.di.coreDomainModule
import com.example.playlistmaker.library.di.libraryPresentationModule
import com.example.playlistmaker.library_new_playlist.di.newPlaylistDataModule
import com.example.playlistmaker.library_new_playlist.di.newPlaylistDomainModule
import com.example.playlistmaker.library_new_playlist.di.newPlaylistPresentationModule
import com.example.playlistmaker.player.di.playerDataModule
import com.example.playlistmaker.player.di.playerDomainModule
import com.example.playlistmaker.player.di.playerPresentationModule
import com.example.playlistmaker.playlist_details.di.playlistDetailsDomainModule
import com.example.playlistmaker.playlist_details.di.playlistDetailsPresentationModule
import com.example.playlistmaker.search.di.data.searchDataModule
import com.example.playlistmaker.search.di.searchDomainModule
import com.example.playlistmaker.search.di.searchPresentationModule
import com.example.playlistmaker.settings.di.settingsDataModule
import com.example.playlistmaker.settings.di.settingsDomainModule
import com.example.playlistmaker.settings.di.settingsPresentationModule
import com.example.playlistmaker.share.di.shareDataModule
import com.example.playlistmaker.share.di.shareDomainModule
import org.koin.dsl.module

val appModule = module {
    includes(coreDataModule, coreDomainModule)
    includes(playerDataModule, playerDomainModule, playerPresentationModule)
    includes(searchDataModule, searchDomainModule, searchPresentationModule)
    includes(settingsDataModule, settingsDomainModule, settingsPresentationModule)
    includes(shareDataModule, shareDomainModule)
    includes(libraryPresentationModule)
    includes(newPlaylistDataModule, newPlaylistDomainModule, newPlaylistPresentationModule)
    includes(playlistDetailsPresentationModule, playlistDetailsDomainModule)
}