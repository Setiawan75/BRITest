package com.news.britest.view.otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.news.britest.databinding.ActivityOtpBinding
import com.news.britest.network.Resource
import com.news.britest.shared.extensions.showDialogError
import com.news.britest.view.success.SuccessView
import com.news.britest.viewmodel.OtpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpView : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context) =
            Intent(context, OtpView::class.java)
    }

    private lateinit var binding: ActivityOtpBinding
    private val viewModel: OtpViewModel by viewModels()
    private lateinit var otpFields: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.modifyToolbar.setTitle("OTP")
        setupOtp()
        setupVerify()

        observe()
    }

    private fun setupOtp() {
        otpFields = listOf(
            binding.otp1,
            binding.otp2,
            binding.otp3,
            binding.otp4,
            binding.otp5,
            binding.otp6
        )

        setupOtpInputs()
    }

    private fun setupOtpInputs() {

        otpFields.forEachIndexed { index, editText ->

            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val text = s.toString()

                    // 🔥 Paste detection
                    if (text.length > 1) {
                        fillOtpFromPaste(text)
                        return
                    }

                    // 👉 Auto move
                    if (text.isNotEmpty()) {
                        if (index < otpFields.size - 1) {
                            otpFields[index + 1].requestFocus()
                        }
                    }

                    updateVerifyButtonState()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            // 🔙 Backspace
            editText.setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL &&
                    event.action == KeyEvent.ACTION_DOWN &&
                    editText.text.isEmpty()
                ) {
                    if (index > 0) {
                        otpFields[index - 1].apply {
                            requestFocus()
                            setSelection(text.length)
                        }
                    }
                }
                false
            }
        }
    }

    private fun fillOtpFromPaste(paste: String) {
        val cleanOtp = paste.take(otpFields.size)

        cleanOtp.forEachIndexed { index, char ->
            otpFields[index].setText(char.toString())
        }

        otpFields.last().requestFocus()
    }

    private fun getOtp(): String {
        return otpFields.joinToString("") { it.text.toString() }
    }

    private fun setupVerify() {
        binding.btnVerify.setOnClickListener {

            val otp = getOtp()

            if (otp.length < 6) {
                Toast.makeText(this, "OTP tidak lengkap", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.verifyOtp(otp)
        }
    }

    private fun updateVerifyButtonState() {
        binding.btnVerify.isEnabled = getOtp().length == 6
    }


    private fun observe() {
        viewModel.state.observe(this) { resource ->
            when (resource) {

                is Resource.Loading -> {
                    isLoading(true)
                    binding.btnVerify.isEnabled = false
                }

                is Resource.Success -> {
                    isLoading()
                    binding.btnVerify.isEnabled = true

                    if (resource.data.success) {
                        startActivity(SuccessView.newIntent(this))
                    } else {
                        showDialogError("Error", resource.data.message)
                    }
                }

                is Resource.Error -> {
                    isLoading()
                    binding.btnVerify.isEnabled = true
                    showDialogError("Error", resource.message)
                }
            }
        }
    }

    private fun isLoading(load: Boolean = false) {
        if (load) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }
}