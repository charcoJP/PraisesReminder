package jp.co.charco.praisesreminder.data.db

import androidx.room.*
import jp.co.charco.praisesreminder.data.db.entity.Praise
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PraiseDao {
    @Query("SELECT * FROM praise")
    suspend fun getAll(): List<Praise>

    @Query("SELECT * FROM praise WHERE date = :date ORDER BY order_no")
    suspend fun getAll(date: LocalDate): List<Praise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: Praise)

    @Delete
    suspend fun delete(entity: Praise)

    @Query("SELECT MAX(`order_no`) FROM praise WHERE date = :date")
    suspend fun getMaxOrder(date: LocalDate): Int
}