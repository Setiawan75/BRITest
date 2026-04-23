package com.news.britest.model

data class Login(
    val success: Boolean,
    val token: String?,
    val message: String
)

data class LoginRequest(
    val username: String,
    val password: String
)
