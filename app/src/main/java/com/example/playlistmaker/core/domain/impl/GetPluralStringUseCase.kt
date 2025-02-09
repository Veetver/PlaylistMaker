package com.example.playlistmaker.core.domain.impl

import com.example.playlistmaker.core.domain.api.StringResolver

class GetPluralStringUseCase(
    private val stringResolver: StringResolver
) {
    operator fun invoke(id: Int, value: Int) = stringResolver.fromPlural(id, value)
}