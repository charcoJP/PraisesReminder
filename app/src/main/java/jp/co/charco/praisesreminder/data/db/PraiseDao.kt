package jp.co.charco.praisesreminder.data.db

import androidx.room.*
import jp.co.charco.praisesreminder.data.db.entity.Praise
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface PraiseDao {
    @Query("SELECT * FROM praise WHERE date = :date")
    fun getAll(date: LocalDate): Flow<List<Praise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: Praise)

    @Delete
    suspend fun delete(entity: Praise)
}