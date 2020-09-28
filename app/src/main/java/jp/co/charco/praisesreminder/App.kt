package jp.co.charco.praisesreminder

import android.app.Application
import androidx.room.Room
import jp.co.charco.praisesreminder.data.db.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    companion object {
        lateinit var database: AppDatabase
    }
}