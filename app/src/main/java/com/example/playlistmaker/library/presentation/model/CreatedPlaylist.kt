package com.example.playlistmaker.library.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class CreatedPlaylist(
    val id: Long,
    val name: String,
    val description: String? = "",
    val trackCount: Int,
    val cover: File?,
    val layout: Int
) : Parcelable