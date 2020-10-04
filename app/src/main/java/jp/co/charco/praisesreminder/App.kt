package jp.co.charco.praisesreminder

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import jp.co.charco.praisesreminder.util.AlarmHelper
import java.util.*

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // TODO: 設定時刻を可変にしたい
        AlarmHelper.createDailyAlarm(this, 0, Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 0)
        })
    }
}