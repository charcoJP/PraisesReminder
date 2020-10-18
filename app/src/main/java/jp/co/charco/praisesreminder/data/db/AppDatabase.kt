package jp.co.charco.praisesreminder.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import jp.co.charco.praisesreminder.data.db.converter.LocalDateTimeConverter
import jp.co.charco.praisesreminder.data.db.converter.LocalDateConverter
import jp.co.charco.praisesreminder.data.db.entity.Praise

@Database(entities = [Praise::class], version = 2)
@TypeConverters(value = [LocalDateTimeConverter::class, LocalDateConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun praiseDao(): PraiseDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE praise ADD order_no INTEGER NOT NULL DEFAULT ${Praise.UNDECIDED_VALUE}")
                // id 順になっているので、id を order に入れておく
                database.execSQL("UPDATE praise set order_no = id")
            }
        }
    }
}