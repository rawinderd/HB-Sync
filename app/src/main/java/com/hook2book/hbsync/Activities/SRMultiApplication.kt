package com.hook2book.hbsync

import android.app.Application
import android.content.Context

class SRMultiApplication : Application() {

    private lateinit var context: Context
    override fun onCreate() {
        super.onCreate()
        this.context = applicationContext
    }

    fun getAppContext1(): Context {
        return context
    }

}