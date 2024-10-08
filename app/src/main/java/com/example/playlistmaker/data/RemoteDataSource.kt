package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Req
import com.example.playlistmaker.data.dto.Res

interface RemoteDataSource {
    fun doRequest(dto: Req): Res
}