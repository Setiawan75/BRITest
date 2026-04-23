package com.news.britest.view.success

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.news.britest.databinding.ActivitySuccessBinding

class SuccessView : AppCompatActivity() {
    companion object{
        fun newIntent(context: Context) = Intent(context, SuccessView::class.java)
    }

    private lateinit var binding: ActivitySuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.modifyToolbar.setTitle("SUCCESS")
    }
}