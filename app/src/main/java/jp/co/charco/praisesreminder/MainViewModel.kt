package jp.co.charco.praisesreminder

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor() : ViewModel() {
    private val now = LocalDate.now()
    private val currentLocalDateSubject = MutableStateFlow(now)

    val currentDateStr: LiveData<String> = currentLocalDateSubject.mapLatest {
        it.format(DateTimeFormatter.ofPattern("yyyy/MM/dd (E)"))
    }.asLiveData()

    fun changeDate(additionalDate: Int) {
        currentLocalDateSubject.value = now.plusDays(additionalDate.toLong())
    }
}