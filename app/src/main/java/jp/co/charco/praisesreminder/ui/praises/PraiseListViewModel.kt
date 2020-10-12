package jp.co.charco.praisesreminder.ui.praises

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import jp.co.charco.praisesreminder.data.db.PraiseDao
import jp.co.charco.praisesreminder.data.db.entity.Praise
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

@ExperimentalCoroutinesApi
class PraiseListViewModel @ViewModelInject constructor(
    private val praiseDao: PraiseDao,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val additionalDate: Long
        get() = savedStateHandle.get<Long>(PraiseListFragment.KEY_ADDITIONAL_DATE) ?: 0

    private val currentLocalDateSubject = MutableStateFlow(LocalDate.now().plusDays(additionalDate))

    val savedPraises = currentLocalDateSubject.flatMapLatest {
        praiseDao.getAll(it)
    }.asLiveData()

    fun delete(praise: Praise) = viewModelScope.launch {
        praiseDao.delete(praise)
    }
}