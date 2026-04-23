package com.news.britest.model

data class OtpRequest(
    val otp: String
)

data class OtpResponse(
    val success: Boolean,
    val message: String
)
