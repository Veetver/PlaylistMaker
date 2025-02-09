package com.example.playlistmaker.core.presentation.ui.utils

import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.updateLayoutParams
import com.example.playlistmaker.R
import com.example.playlistmaker.util.mapper.DpToPxConverter.dpToPx
import com.google.android.material.snackbar.Snackbar

fun playlistmakerSnackbar(view: View, text: String): Snackbar {
    val snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT)

    snackbar.view.background = ContextCompat.getDrawable(view.context, R.drawable.snackbar_bg)
    snackbar.view.updateLayoutParams<FrameLayout.LayoutParams> {
        bottomMargin = dpToPx(16f, view.context)
        marginStart = dpToPx(8f, view.context)
        marginEnd = dpToPx(8f, view.context)
    }

    snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).apply {
        ellipsize = TextUtils.TruncateAt.MIDDLE
        maxLines = 2
        textAlignment = View.TEXT_ALIGNMENT_CENTER
    }
    return snackbar
}