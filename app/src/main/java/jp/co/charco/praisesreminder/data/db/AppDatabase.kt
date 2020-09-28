package jp.co.charco.praisesreminder.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import jp.co.charco.praisesreminder.data.db.entity.Praise

@Database(entities = [Praise::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun praiseDao(): PraiseDao
}