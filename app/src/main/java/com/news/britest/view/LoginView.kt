package com.news.britest.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.news.britest.databinding.ActivityLoginBinding
import com.news.britest.network.Resource
import com.news.britest.shared.extensions.showDialogError
import com.news.britest.view.otp.OtpView
import com.news.britest.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginView : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.etUsername.text.toString(), binding.etPassword.text.toString())
        }

        observeLogin()
    }

    private fun isLoading(load: Boolean = false) {
        if (load) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

    private fun observeLogin() {
        viewModel.loginState.observe(this) { result ->
            when (result) {
                is Resource.Loading -> {
                    isLoading(true)
                }

                is Resource.Success -> {
                    isLoading()
                    if (result.data.success) {
                        startActivity(OtpView.newIntent(this))
                    } else {
                        showDialogError("Login Gagal", result.data.message)
                    }
                }

                is Resource.Error -> {
                    isLoading()
                    showDialogError("Error", result.message)
                }
            }
        }
    }
}