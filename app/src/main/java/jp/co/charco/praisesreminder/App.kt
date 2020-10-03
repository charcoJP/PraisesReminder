package jp.co.charco.praisesreminder

import android.app.Application
import androidx.room.Room
import jp.co.charco.praisesreminder.data.db.AppDatabase
import jp.co.charco.praisesreminder.util.AlarmHelper
import java.util.*


class App : Application() {
    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

        // TODO: 設定時刻を可変にしたい
        AlarmHelper.createDailyAlarm(this, 0, Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 19)
            set(Calendar.MINUTE, 0)
        })
    }

    companion object {
        lateinit var database: AppDatabase
    }
}