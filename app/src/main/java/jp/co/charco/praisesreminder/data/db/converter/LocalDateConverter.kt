package jp.co.charco.praisesreminder.data.db.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(localDate: LocalDate): Long =
        localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    @TypeConverter
    fun toLocalDate(epochMilli: Long): LocalDate =
        Instant.ofEpochMilli(epochMilli).atZone(ZoneId.systemDefault()).toLocalDate()
}