package com.example.playlistmaker.share.domain.usecase

import com.example.playlistmaker.share.domain.api.ShareRepository

class ShareAppUseCase(
    private val shareRepository: ShareRepository
) {
    fun execute() {
        shareRepository.shareApp()
    }
}