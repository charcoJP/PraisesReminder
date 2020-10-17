package jp.co.charco.praisesreminder.data.db

import androidx.room.Room
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

class AppDatabaseTest {

    private val TEST_DB = "migration-test"

    @JvmField
    @Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }


    @Test
    @Throws(IOException::class)
    fun migrate1To2() = runBlocking {
        val contents = listOf("1", "2", "3")

        helper.createDatabase(TEST_DB, 1).apply {
            // db has schema version 1. insert some data using SQL queries.
            // You cannot use DAO classes because they expect the latest schema.
            contents.forEach {
                execSQL("INSERT INTO praise(content, date, created_at) values('$it', 0, 0)")
            }
            // Prepare for the next version.
            close()
        }

        // スキーマの検証
        val db = helper.runMigrationsAndValidate(TEST_DB, 2, true, AppDatabase.MIGRATION_1_2)
        db.close()

        // データの移行の検証
        val migratedDatabase = getMigratedRoomDatabase()
        val dao = migratedDatabase.praiseDao()
        val result = dao.getAll()

        result.forEachIndexed { index, entity ->
            val expectedId = index + 1
            assertEquals(expectedId, entity.id)
            assertEquals(contents[index], entity.content)
            assertEquals(expectedId, entity.orderNo)
        }
    }

    private fun getMigratedRoomDatabase(): AppDatabase {
        return Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AppDatabase::class.java,
            TEST_DB
        ).addMigrations(
            AppDatabase.MIGRATION_1_2
        ).build()
    }
}