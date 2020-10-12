package jp.co.charco.praisesreminder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import jp.co.charco.praisesreminder.data.db.PraiseDao
import jp.co.charco.praisesreminder.data.db.entity.Praise
import jp.co.charco.praisesreminder.util.Event
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val praiseDao: PraiseDao
) : ViewModel() {
    private val now = LocalDate.now()
    private val currentLocalDateSubject = MutableStateFlow(now)
    val currentEpochMilli: Long
        get() = currentLocalDateSubject.value.atStartOfDay().toInstant(ZoneOffset.UTC)
            .toEpochMilli()

    private val _successSubmit = MutableLiveData<Event<Unit>>()
    val successSubmit: LiveData<Event<Unit>> = _successSubmit

    val currentDateStr: LiveData<String> = currentLocalDateSubject.mapLatest {
        it.format(DateTimeFormatter.ofPattern("yyyy/MM/dd (E)"))
    }.asLiveData()

    private val _showInputBottomSheet = MutableLiveData<Event<Praise>>()
    val showInputBottomSheet: LiveData<Event<Praise>> = _showInputBottomSheet.distinctUntilChanged()

    fun changeDate(additionalDate: Int) {
        currentLocalDateSubject.value = now.plusDays(additionalDate.toLong())
    }

    fun changeDate(epochMilli: Long) = viewModelScope.launch {
        currentLocalDateSubject.value = Instant.ofEpochMilli(epochMilli)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    fun submit(praise: Praise) = viewModelScope.launch {
        praiseDao.save(praise)
        _successSubmit.value = Event(Unit)
    }

    fun showInputBottomSheet(praise: Praise) {
        _showInputBottomSheet.value = Event(praise)
    }
}