package com.example.news_common

import android.util.Log

interface Logger {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, tr: Throwable?)
}

fun AndroidLogCatLogger() = object : Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String, tr: Throwable?) {
        Log.e(tag, message + tr, tr)
    }
}