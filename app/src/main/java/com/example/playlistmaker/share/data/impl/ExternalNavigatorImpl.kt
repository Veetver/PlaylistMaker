package com.example.playlistmaker.share.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.share.data.ExternalNavigator
import com.example.playlistmaker.share.data.model.ActionSendPayload
import com.example.playlistmaker.share.data.model.ActionSendToPayload
import com.example.playlistmaker.share.data.model.ActionViewPayload

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun sendText(payload: ActionSendPayload) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, payload.text)
            setType("text/plain")
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    override fun sendMail(payload: ActionSendToPayload) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(payload.email))
            putExtra(Intent.EXTRA_SUBJECT, payload.subject)
            putExtra(Intent.EXTRA_TEXT, payload.text)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }

    override fun openUrl(payload: ActionViewPayload) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            Uri.parse(payload.url)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        context.startActivity(intent)
    }
}