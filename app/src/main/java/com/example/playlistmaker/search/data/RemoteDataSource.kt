package com.example.playlistmaker.search.data

import com.example.playlistmaker.search.data.dto.Req
import com.example.playlistmaker.search.data.dto.Res

interface RemoteDataSource {
    fun doRequest(dto: Req): Res
}