package com.cloudfiveapp.android.push

import android.app.Notification.DEFAULT_SOUND
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cloudfiveapp.android.R
import com.cloudfiveapp.push.PushMessageReceiver
import io.reactivex.disposables.CompositeDisposable
import java.lang.reflect.Type

internal class PushNotificationReceiver(context: Context) : PushMessageReceiver {
    companion object {
        private const val NOTIFICATION_ID_CLOUD_FIVE = 0
        private const val ANNOUNCEMENT_CHANNEL_ID = "com.cloudfiveapp.android.ANNOUNCEMENT_CHANNEL"
    }

    private val context: Context = context.applicationContext
    private val notificationManager: NotificationManager

    init {
        notificationManager = this.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val announcementsChannel = NotificationChannel(
                    ANNOUNCEMENT_CHANNEL_ID,
                    "Announcements",
                    NotificationManager.IMPORTANCE_DEFAULT)
            announcementsChannel.description = "Announcements"
            notificationManager.createNotificationChannel(announcementsChannel)
        }
    }

    override fun onPushMessageReceived(intent: Intent) {
        val alert = intent.extras?.getString("alert")
        val message = intent.extras?.getString("message")
        val notification = getBaseBuilder(alert, message).build()
        notificationManager.notify(NOTIFICATION_ID_CLOUD_FIVE, notification)
    }

    private fun getBaseBuilder(title: String?, text: String?): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, ANNOUNCEMENT_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setAutoCancel(true)
                .setDefaults(DEFAULT_SOUND)
                .setContentTitle(title)
                .setContentText(text)
    }
}