package com.news.britest.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.news.britest.model.Login
import com.news.britest.network.Resource
import com.news.britest.repository.LoginRepo
import com.news.britest.shared.utils.getOrAwaitValue
import com.news.britest.viewmodel.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    private val repository: LoginRepo = mockk()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setup() {
        viewModel = LoginViewModel(repository)
    }

    @Test
    fun `login success emit Success`() = runTest {
        val response = Login(true, "token123", "Login success")

        coEvery {
            repository.login("admin", "123456")
        } returns response

        viewModel.login("admin", "123456")

        val resource = viewModel.loginState.getOrAwaitValue()

        TestCase.assertTrue(resource is Resource.Success)
        if (resource is Resource.Success) {
            assertTrue(resource.data.success)
        }
    }

    @Test
    fun `login failure emit Success with false`() = runTest {
        val response = Login(false, null, "Invalid")

        coEvery {
            repository.login(any(), any())
        } returns response

        viewModel.login("wrong", "wrong")

        val resource = viewModel.loginState.getOrAwaitValue()

        TestCase.assertTrue(resource is Resource.Success)
        if (resource is Resource.Success) {
            assertFalse(resource.data.success)
        }
    }

    @Test
    fun `login error emit Error`() = runTest {

        coEvery {
            repository.login(any(), any())
        } throws Exception("Network error")

        viewModel.login("admin", "123456")

        val resource = viewModel.loginState.getOrAwaitValue()

        TestCase.assertTrue(resource is Resource.Error)
    }
}