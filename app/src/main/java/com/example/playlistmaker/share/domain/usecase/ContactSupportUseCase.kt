package com.example.playlistmaker.share.domain.usecase

import com.example.playlistmaker.share.domain.api.ShareRepository

class ContactSupportUseCase(
    private val shareRepository: ShareRepository
) {
    fun execute() {
        shareRepository.contactSupport()
    }
}