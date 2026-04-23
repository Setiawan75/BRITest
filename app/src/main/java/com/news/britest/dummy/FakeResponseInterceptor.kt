package com.news.britest.dummy

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

class FakeResponseInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.url.encodedPath.contains("login")) {
            Thread.sleep(1500)

            val requestBody = request.body
            val buffer = okio.Buffer()
            requestBody?.writeTo(buffer)
            val bodyString = buffer.readUtf8()

            // Parse JSON request
            val jsonObject = JSONObject(bodyString)
            val username = jsonObject.optString("username")
            val password = jsonObject.optString("password")

            val isSuccess = username == "admin" && password == "123456"

            val jsonResponse = if (isSuccess) {
                """
                {
                  "success": true,
                  "token": "fake_jwt_token_123",
                  "message": "Login success"
                }
                """.trimIndent()
            } else {
                """
                {
                  "success": false,
                  "token": null,
                  "message": "Invalid username or password"
                }
                """.trimIndent()
            }

            return Response.Builder()
                .code(200)
                .message(jsonResponse)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(
                    jsonResponse.toByteArray()
                        .toResponseBody("application/json".toMediaType())
                )
                .addHeader("content-type", "application/json")
                .build()
        }

        return chain.proceed(request)
    }
}