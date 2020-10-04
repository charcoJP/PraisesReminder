package jp.co.charco.praisesreminder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import jp.co.charco.praisesreminder.data.db.PraiseDao
import jp.co.charco.praisesreminder.data.db.entity.Praise
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    private val praiseDao: PraiseDao
) : ViewModel() {

    private val _successSubmit = MutableLiveData<Unit>()
    val successSubmit: LiveData<Unit> = _successSubmit

    private val currentLocalDateSubject = MutableStateFlow(LocalDate.now())
    val currentEpochMilli: Long
        get() = currentLocalDateSubject.value.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()

    val savedPraises = currentLocalDateSubject.flatMapLatest {
        praiseDao.getAll(it)
    }.asLiveData()

    val currentDateStr: LiveData<String> = currentLocalDateSubject.mapLatest {
        it.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
    }.asLiveData()

    fun changeDate(epochMilli: Long) = viewModelScope.launch {
        currentLocalDateSubject.value = Instant.ofEpochMilli(epochMilli)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    fun submit(input: String) = viewModelScope.launch {
        praiseDao.save(Praise(content = input))
        _successSubmit.value = Unit
    }

    fun delete(praise: Praise) = viewModelScope.launch {
        praiseDao.delete(praise)
    }
}