package jp.co.charco.praisesreminder.util

import jp.co.charco.praisesreminder.data.db.converter.LocalDateConverter
import java.time.LocalDate

fun LocalDate.toUtcEpochMilli(): Long = LocalDateConverter().fromLocalDate(this)
fun Long.toUtcLocalDate(): LocalDate = LocalDateConverter().toLocalDate(this)
