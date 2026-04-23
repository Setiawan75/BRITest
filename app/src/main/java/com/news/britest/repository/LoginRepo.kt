package com.news.britest.repository

import com.news.britest.model.Login
import com.news.britest.model.LoginRequest
import com.news.britest.network.API
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class LoginRepo @Inject constructor(
    private val api: API
) {
    suspend fun login(username: String, password: String): Login {
        try {
            return api.login(LoginRequest(username, password))
        } catch (e: HttpException) {
            throw Exception("Server error: ${e.code()}")
        } catch (e: IOException) {
            throw Exception("Network error")
        }
    }
}