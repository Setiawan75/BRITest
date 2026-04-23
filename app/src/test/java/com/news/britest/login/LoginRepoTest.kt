package com.news.britest.login

import com.news.britest.model.Login
import com.news.britest.network.API
import com.news.britest.repository.LoginRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginRepoTest {

    private val api: API = mockk()
    private lateinit var repository: LoginRepo

    @Before
    fun setup() {
        repository = LoginRepo(api)
    }

    @Test
    fun `login success`() = runTest {
        val response = Login(true, "token123", "Login success")

        coEvery {
            api.login(any())
        } returns response

        val result = repository.login("admin", "123456")

        assertTrue(result.success)
        assertEquals("token123", result.token)
    }

    @Test
    fun `login failure`() = runTest {
        val response = Login(false, null, "Invalid")

        coEvery {
            api.login(any())
        } returns response

        val result = repository.login("wrong", "wrong")

        assertFalse(result.success)
        assertNull(result.token)
    }
}