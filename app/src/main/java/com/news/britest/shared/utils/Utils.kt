package com.news.britest.shared.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.news.britest.network.Resource
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.getOrAwaitValue(): T {
    var data: T? = null
    val latch = CountDownLatch(1)

    val observer = object : Observer<T> {
        override fun onChanged(value: T) {
            if (value !is Resource.Loading) {
                data = value
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
    }

    this.observeForever(observer)
    latch.await(2, TimeUnit.SECONDS)

    return data!!
}