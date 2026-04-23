package com.news.britest.otp

import com.news.britest.model.OtpRequest
import com.news.britest.model.OtpResponse
import com.news.britest.network.API
import com.news.britest.repository.OtpRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OtpRepositoryTest {

    private val api: API = mockk()
    private lateinit var repository: OtpRepo

    @Before
    fun setup() {
        repository = OtpRepo(api)
    }

    @Test
    fun `verify OTP success`() = runTest {
        val response = OtpResponse(true, "OTP verified successfully")

        coEvery {
            api.verifyOtp(OtpRequest("123456"))
        } returns response

        val result = repository.verifyOtp("123456")

        assertTrue(result.success)
        assertEquals("OTP verified successfully", result.message)
    }

    @Test
    fun `verify OTP failure`() = runTest {
        val response = OtpResponse(false, "Invalid OTP")

        coEvery {
            api.verifyOtp(any())
        } returns response

        val result = repository.verifyOtp("000000")

        assertFalse(result.success)
        assertEquals("Invalid OTP", result.message)
    }
}