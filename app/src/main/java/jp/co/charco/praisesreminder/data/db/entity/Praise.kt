package jp.co.charco.praisesreminder.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Praise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val content: String,
    @ColumnInfo(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now()
)