package com.example.playlistmaker.share.di

import com.example.playlistmaker.share.data.ExternalNavigator
import com.example.playlistmaker.share.data.ShareRepositoryImpl
import com.example.playlistmaker.share.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.share.domain.api.ShareRepository
import org.koin.dsl.module

val shareDataModule = module {
    single<ShareRepository> {
        ShareRepositoryImpl(context = get(), externalNavigator = get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(context = get())
    }
}