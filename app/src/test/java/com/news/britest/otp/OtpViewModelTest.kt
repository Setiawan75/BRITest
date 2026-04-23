package com.news.britest.otp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.news.britest.model.OtpResponse
import com.news.britest.network.Resource
import com.news.britest.repository.OtpRepo
import com.news.britest.shared.utils.getOrAwaitValue
import com.news.britest.viewmodel.OtpViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class OtpViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val repository: OtpRepo = mockk()
    private lateinit var viewModel: OtpViewModel

    @Before
    fun setup() {
        viewModel = OtpViewModel(repository)
    }

    @Test
    fun `verify OTP success emit Success`() = runTest {
        val response = OtpResponse(true, "Success")

        coEvery {
            repository.verifyOtp("123456")
        } returns response

        viewModel.verifyOtp("123456")

        advanceUntilIdle()

        val resource = viewModel.state.getOrAwaitValue()

        assertTrue(resource is Resource.Success)
        assertTrue((resource as Resource.Success).data.success)
    }

    @Test
    fun `verify OTP failure emit Success with false`() = runTest {
        val response = OtpResponse(false, "Invalid OTP")

        coEvery {
            repository.verifyOtp(any())
        } returns response

        viewModel.verifyOtp("000000")

        advanceUntilIdle()

        val resource = viewModel.state.getOrAwaitValue()

        assertTrue(resource is Resource.Success)
        assertFalse((resource as Resource.Success).data.success)
    }

    @Test
    fun `verify OTP error emit Error`() = runTest {

        coEvery {
            repository.verifyOtp(any())
        } throws Exception("Network error")

        viewModel.verifyOtp("123456")

        advanceUntilIdle()

        val resource = viewModel.state.getOrAwaitValue()

        assertTrue(resource is Resource.Error)
    }
}