package com.example.playlistmaker.search.domain.model

sealed class Resource<T>(val data: T? = null, val errorCode: Int? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(errorCode: Int, data: T? = null): Resource<T>(data, errorCode)
}