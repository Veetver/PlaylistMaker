package com.example.playlistmaker.share.domain.usecase

import com.example.playlistmaker.share.domain.api.ShareRepository

class OpenEulaUseCase(
    private val shareRepository: ShareRepository
) {
    fun execute() {
        shareRepository.openEula()
    }
}