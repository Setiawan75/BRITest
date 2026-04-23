package com.news.britest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.news.britest.model.OtpResponse
import com.news.britest.network.Resource
import com.news.britest.repository.OtpRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val repository: OtpRepo
) : ViewModel() {

    private val _state = MutableLiveData<Resource<OtpResponse>>()
    val state: LiveData<Resource<OtpResponse>> = _state

    fun verifyOtp(otp: String) {
        _state.value = Resource.Loading

        viewModelScope.launch {
            try {
                val response = repository.verifyOtp(otp)
                _state.value = Resource.Success(response)
            } catch (e: Exception) {
                _state.value = Resource.Error(e.message ?: "Error")
            }
        }
    }
}