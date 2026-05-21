package com.example

import android.app.Application
import android.content.Intent
import android.util.Log

class PasuAhaarApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Custom Global Exception Handler
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Log.e("PasuAhaarCrash", "App crashed: ${throwable.message}", throwable)
            
            // Launch our Crash Handler Activity to simulate AI Agent fixing
            val intent = Intent(this, ErrorActivity::class.java).apply {
                putExtra("ERROR_DETAILS", throwable.stackTraceToString())
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            
            // Kill the current process
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)
        }
    }
}
