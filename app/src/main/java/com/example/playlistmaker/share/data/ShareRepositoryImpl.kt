package com.example.playlistmaker.share.data

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.share.data.model.ActionSendPayload
import com.example.playlistmaker.share.data.model.ActionSendToPayload
import com.example.playlistmaker.share.data.model.ActionViewPayload
import com.example.playlistmaker.share.domain.api.ShareRepository

class ShareRepositoryImpl(
    private val context: Context,
    private val externalNavigator: ExternalNavigator
) : ShareRepository {
    override fun shareApp() {
        externalNavigator.sendText(
            ActionSendPayload(
                text = context.getString(R.string.course_link)
            )
        )
    }

    override fun contactSupport() {
        externalNavigator.sendMail(
            ActionSendToPayload(
                email = context.getString(R.string.email_template),
                subject = context.getString(R.string.subject_email_template),
                text = context.getString(R.string.text_email_template),
            )
        )
    }

    override fun openEula() {
        externalNavigator.openUrl(
            ActionViewPayload(
                url = context.getString(R.string.eula_link)
            )
        )
    }
}