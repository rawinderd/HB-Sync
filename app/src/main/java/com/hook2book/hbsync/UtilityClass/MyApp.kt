package com.hook2book.hbsync.UtilityClass

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class MyApp : Application(), CoroutineScope by CoroutineScope(SupervisorJob() + Dispatchers.Default) {

    override fun onTerminate() {
        // Cancel all coroutines when the app is terminated (for safety)
        cancel()
        super.onTerminate()
    }
}