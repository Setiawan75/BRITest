package com.news.britest.network
import com.news.britest.model.Login
import com.news.britest.model.LoginRequest
import com.news.britest.model.OtpRequest
import com.news.britest.model.OtpResponse
import kotlinx.coroutines.delay
import retrofit2.http.Body
import retrofit2.http.POST

interface API {
    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Login

    @POST("verify-otp")
    suspend fun verifyOtp(
        @Body request: OtpRequest
    ): OtpResponse

}