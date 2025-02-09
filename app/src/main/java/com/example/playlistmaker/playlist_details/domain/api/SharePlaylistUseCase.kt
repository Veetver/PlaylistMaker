package com.example.playlistmaker.playlist_details.domain.api

import com.example.playlistmaker.share.data.ExternalNavigator
import com.example.playlistmaker.share.data.model.ActionSendPayload

class SharePlaylistUseCase(
    private val externalNavigator: ExternalNavigator
) {
    operator fun invoke(text: String) =
        externalNavigator.sendText(
            payload = ActionSendPayload(text)
        )

}