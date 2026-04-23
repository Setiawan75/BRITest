package com.news.britest.dummy

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject

class FakeOtpInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        if (request.url.encodedPath.contains("verify-otp")) {

            Thread.sleep(1500) // simulate network delay

            val buffer = okio.Buffer()
            request.body?.writeTo(buffer)
            val bodyString = buffer.readUtf8()

            val json = JSONObject(bodyString)
            val otp = json.optString("otp")

            val isValid = otp == "123456"

            val jsonResponse = if (isValid) {
                """
                {
                  "success": true,
                  "message": "OTP verified successfully"
                }
                """
            } else {
                """
                {
                  "success": false,
                  "message": "Invalid OTP"
                }
                """
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