package com.example.altu.api

class AltuApiException(
    message: String,
    val statusCode: Int? = null,
) : Exception(message)
