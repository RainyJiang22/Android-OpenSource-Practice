package com.rainy.monitor.holder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.LongSparseArray
import androidx.core.app.NotificationCompat
import com.rainy.monitor.Monitor.getLaunchIntent
import com.rainy.monitor.R
import com.rainy.monitor.db.HttpInformation

/**
 * @author jiangshiyu
 * @date 2022/9/13
 */
internal object NotificationHolder {

    private const val CHANNEL_ID = "monitorLeavesChannelId"

    private const val CHANNEL_NAME = "Http Notifications"

    private const val NOTIFICATION_TITLE = "Recording Http"

    private const val NOTIFICATION_ID = 19950724

    private const val BUFFER_SIZE = 10

    private val transactionBuffer = LongSparseArray<HttpInformation>()

    private val context: Context
        get() = ContextHolder.context

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var transactionCount: Int = 0

    @Volatile
    private var showNotification = true

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_MIN
                )
            )
        }
    }

    @Synchronized
    fun show(transaction: HttpInformation) {
        if (showNotification) {
            addToBuffer(transaction)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentIntent(getContentIntent(context))
                .setLocalOnly(true)
                .setOnlyAlertOnce(true)
                .setSound(null)
                .setSmallIcon(R.drawable.icon_monitor_launcher)
                .setContentTitle(NOTIFICATION_TITLE)
                .setAutoCancel(false)
            val inboxStyle = NotificationCompat.InboxStyle()
            val size = transactionBuffer.size()
            if (size > 0) {
                builder.setContentText(transactionBuffer.valueAt(size - 1).notificationText)
                for (i in size - 1 downTo 0) {
                    inboxStyle.addLine(transactionBuffer.valueAt(i).notificationText)
                }
            }
            builder.setStyle(inboxStyle)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setSubText(transactionCount.toString())
            } else {
                builder.setNumber(transactionCount)
            }
            notificationManager.notify(NOTIFICATION_ID, builder.build())
        }
    }

    @Synchronized
    private fun addToBuffer(httpInformation: HttpInformation) {
        transactionCount++
        transactionBuffer.put(httpInformation.id, httpInformation)
        if (transactionBuffer.size() > BUFFER_SIZE) {
            transactionBuffer.removeAt(0)
        }
    }

    @Synchronized
    fun showNotification(showNotification: Boolean) {
        this.showNotification = showNotification
    }

    @Synchronized
    fun clearBuffer() {
        transactionBuffer.clear()
        transactionCount = 0
    }

    @Synchronized
    fun dismiss() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun getContentIntent(context: Context): PendingIntent {
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(
            context,
            100,
            getLaunchIntent(context),
            flag
        )
    }

}