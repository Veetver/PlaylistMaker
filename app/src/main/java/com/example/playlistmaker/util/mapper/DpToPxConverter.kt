package com.example.playlistmaker.util.mapper

import android.content.Context
import android.util.TypedValue

object DpToPxConverter {
    fun dpToPx(dp: Float, context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            context.resources.displayMetrics
        ).toInt()
    }
}