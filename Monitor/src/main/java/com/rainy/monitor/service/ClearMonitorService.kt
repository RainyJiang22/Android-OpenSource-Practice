package com.rainy.monitor.service

import android.app.IntentService
import android.content.Intent
import com.rainy.monitor.Monitor

internal class ClearMonitorService : IntentService(ClearMonitorService::class.java.name) {

    override fun onHandleIntent(intent: Intent?) {
        Monitor.clearNotification()
    }

}