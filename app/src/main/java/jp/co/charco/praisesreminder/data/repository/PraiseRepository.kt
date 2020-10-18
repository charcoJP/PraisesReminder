package jp.co.charco.praisesreminder.data.repository

import jp.co.charco.praisesreminder.data.db.PraiseDao
import jp.co.charco.praisesreminder.data.db.entity.Praise
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import javax.inject.Inject

class PraiseRepository @Inject constructor(private val praiseDao: PraiseDao) {
    suspend fun getPraises(date: LocalDate) = flow {
        val result = praiseDao.getAll(date)
        emit(result)
    }

    suspend fun save(praise: Praise) {
        val orderNo = (praiseDao.getMaxOrder(praise.date) ?: 0) + 1
        praise.orderNo = orderNo
        praiseDao.save(praise)
    }

    suspend fun updateOrders(mutableList: MutableList<Praise>) {
        // TODO: 件数が少ないので current の日付分、すべて order を付け直す
        mutableList.forEachIndexed { index, praise ->
            praise.orderNo = index + 1

            praiseDao.update(praise)
        }
    }

    suspend fun delete(praise: Praise) {
        return praiseDao.delete(praise)
    }
}