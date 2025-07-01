package com.sikhreader.srmulti

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.hilt.android.HiltAndroidApp

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