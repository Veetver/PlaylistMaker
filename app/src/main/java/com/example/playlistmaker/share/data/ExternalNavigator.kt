package com.example.playlistmaker.share.data

import com.example.playlistmaker.share.data.model.ActionSendPayload
import com.example.playlistmaker.share.data.model.ActionSendToPayload
import com.example.playlistmaker.share.data.model.ActionViewPayload


interface ExternalNavigator {
    fun sendText(payload: ActionSendPayload)
    fun sendMail(payload: ActionSendToPayload)
    fun openUrl(payload: ActionViewPayload)
}