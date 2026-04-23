package com.news.britest.viewmodel

import androidx.lifecycle.*
import com.news.britest.model.Login
import com.news.britest.network.Resource
import com.news.britest.repository.LoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: LoginRepo
) : ViewModel() {

    private val _loginState = MutableLiveData<Resource<Login>>()
    val loginState: LiveData<Resource<Login>> = _loginState

    fun login(username: String, password: String) {
        _loginState.value = Resource.Loading

        viewModelScope.launch {
                try {
                    val response = repository.login(username, password)
                    _loginState.value = Resource.Success(response)
                } catch (e: Exception) {
                    _loginState.value = Resource.Error(e.message ?: "Unknown error")
                }
        }
    }
}