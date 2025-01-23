package com.example.playlistmaker.library.presentation.model

import java.io.File

data class CreatedPlaylist(val id: Long, val name: String, val trackCount: Int, val cover: File?, val layout: Int)