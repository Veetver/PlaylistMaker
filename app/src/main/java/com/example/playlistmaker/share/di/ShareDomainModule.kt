package com.example.playlistmaker.share.di

import com.example.playlistmaker.share.domain.usecase.ContactSupportUseCase
import com.example.playlistmaker.share.domain.usecase.OpenEulaUseCase
import com.example.playlistmaker.share.domain.usecase.ShareAppUseCase
import org.koin.dsl.module

val shareDomainModule = module {
    factory {
        ContactSupportUseCase(shareRepository = get())
    }

    factory {
        OpenEulaUseCase(shareRepository = get())
    }

    factory {
        ShareAppUseCase(shareRepository = get())
    }
}