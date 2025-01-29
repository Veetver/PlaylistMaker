package com.example.playlistmaker.core.domain.impl

import com.example.playlistmaker.core.domain.api.StringResolver

class GetStringFromIdUseCase(
    private val stringResolver: StringResolver
) {
    operator fun invoke(id: Int) = stringResolver.fromStringId(id)
}