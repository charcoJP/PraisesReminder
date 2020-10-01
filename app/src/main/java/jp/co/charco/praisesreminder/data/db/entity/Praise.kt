package jp.co.charco.praisesreminder.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Entity
data class Praise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    val date: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    @Ignore
    val dateStr: String = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
}