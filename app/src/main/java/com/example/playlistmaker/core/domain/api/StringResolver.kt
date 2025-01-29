package com.example.playlistmaker.core.domain.api

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface StringResolver {
    fun fromStringId(@StringRes id: Int): String
    fun fromPlural(@PluralsRes id: Int, value: Int): String
}
