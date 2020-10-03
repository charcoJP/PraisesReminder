package jp.co.charco.praisesreminder.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.content.getSystemService
import jp.co.charco.praisesreminder.receiver.AlarmReceiver
import java.util.*

object AlarmHelper {
    fun createDailyAlarm(
        context: Context,
        requestCode: Int,
        time: Calendar
    ) {
        val alarmManager = context.getSystemService<AlarmManager>()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.KEY_REQUEST_CODE, requestCode)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)

        alarmManager?.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}