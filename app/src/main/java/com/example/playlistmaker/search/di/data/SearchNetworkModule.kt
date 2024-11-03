package com.example.playlistmaker.search.di.data

import com.example.playlistmaker.search.data.network.ITunesApiService
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val searchNetworkModule = module {
    single { provideConverterFactory() }
    single { provideBaseUrl() }
    single {
        provideRetrofit(
            baseUrl = get(),
            gsonConverterFactory = get()
        )
    }
    single { provideService(retrofit = get()) }
}

fun provideBaseUrl() = "https://itunes.apple.com"

private fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

private fun provideRetrofit(
    baseUrl: String,
    gsonConverterFactory: GsonConverterFactory,
): Retrofit {
    return Retrofit
        .Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(gsonConverterFactory)
        .build()
}

private fun provideService(retrofit: Retrofit): ITunesApiService =
    retrofit.create(ITunesApiService::class.java)