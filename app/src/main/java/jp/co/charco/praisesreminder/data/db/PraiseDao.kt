package jp.co.charco.praisesreminder.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import jp.co.charco.praisesreminder.data.db.entity.Praise

@Dao
interface PraiseDao {
    @Query("SELECT * FROM praise")
    suspend fun getAll(): List<Praise>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: Praise)
}