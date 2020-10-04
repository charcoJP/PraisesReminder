package jp.co.charco.praisesreminder.data.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Parcelize
@Entity
data class Praise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var content: String,
    var date: LocalDate = LocalDate.now(),
    // TODO: insert するタイミングの now を利用したい
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
) : Parcelable {
    @Ignore
    val dateStr: String = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))

    companion object {
        fun empty() = Praise(content = "")
    }
}