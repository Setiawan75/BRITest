package com.news.britest.repository

import com.news.britest.model.OtpRequest
import com.news.britest.model.OtpResponse
import com.news.britest.network.API
import javax.inject.Inject

class OtpRepo @Inject constructor(
    private val api: API
) {

    suspend fun verifyOtp(otp: String): OtpResponse {
        return api.verifyOtp(OtpRequest(otp))
    }
}