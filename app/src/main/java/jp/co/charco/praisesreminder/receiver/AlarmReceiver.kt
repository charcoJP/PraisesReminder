package jp.co.charco.praisesreminder.receiver

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import jp.co.charco.praisesreminder.MainActivity


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val requestCode = intent.getIntExtra(KEY_REQUEST_CODE, INVALID_REQUEST_CODE).also {
            if (it == INVALID_REQUEST_CODE) throw IllegalStateException("Request Code is empty.")
        }

        val openIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification　Channel 設定
        val channelId = "alarm"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alarm",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("今日入力した内容を確認しよう")
            .setSmallIcon(R.drawable.ic_lock_idle_alarm)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setWhen(System.currentTimeMillis())
            .build()
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val KEY_REQUEST_CODE = "KEY_REQUEST_CODE"
        private const val INVALID_REQUEST_CODE = -1
        private const val NOTIFICATION_ID = 1
    }
}