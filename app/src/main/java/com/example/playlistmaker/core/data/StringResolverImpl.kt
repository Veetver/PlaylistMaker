package com.example.playlistmaker.core.data

import android.content.Context
import com.example.playlistmaker.core.domain.api.StringResolver

class StringResolverImpl(
    private val context: Context
) : StringResolver {
    override fun fromStringId(id: Int): String = context.resources.getString(id)
    override fun fromPlural(id: Int, value: Int): String = context.resources.getQuantityString(id, value, value)
}