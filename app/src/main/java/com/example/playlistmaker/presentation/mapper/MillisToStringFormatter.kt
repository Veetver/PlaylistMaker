package com.example.playlistmaker.presentation.mapper

import java.text.SimpleDateFormat
import java.util.Locale

object MillisToStringFormatter {
    fun millisToStringFormatter(millis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }
}
