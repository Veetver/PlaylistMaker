package com.example.playlistmaker.core.di

import com.example.playlistmaker.core.domain.impl.GetPluralStringUseCase
import com.example.playlistmaker.core.domain.impl.GetStringFromIdUseCase
import org.koin.dsl.module

val coreDomainModule = module {
    factory {
        GetPluralStringUseCase(
            stringResolver = get()
        )
    }

    factory {
        GetStringFromIdUseCase(
            stringResolver = get()
        )
    }
}