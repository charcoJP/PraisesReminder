package jp.co.charco.praisesreminder

import android.app.Application
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import jp.co.charco.praisesreminder.data.datastore.SettingDataStore
import jp.co.charco.praisesreminder.data.db.entity.Praise
import jp.co.charco.praisesreminder.data.repository.PraiseRepository
import jp.co.charco.praisesreminder.util.AlarmHelper
import jp.co.charco.praisesreminder.util.Event
import jp.co.charco.praisesreminder.util.singleLiveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

@ExperimentalCoroutinesApi
class MainViewModel @ViewModelInject constructor(
    application: Application,
    private val praiseRepository: PraiseRepository,
    private val settingDataStore: SettingDataStore
) : AndroidViewModel(application) {
    private val now = LocalDate.now()
    private val currentLocalDateSubject = MutableStateFlow(now)
    val currentEpochMilli: Long
        get() = currentLocalDateSubject.value.atStartOfDay().toInstant(ZoneOffset.UTC)
            .toEpochMilli()

    private val _successSubmit = singleLiveData<Unit>()
    val successSubmit: LiveData<Unit> = _successSubmit

    private val _changePageEvent = MutableLiveData<Event<Int>>()
    val changePageEvent: LiveData<Event<Int>> = _changePageEvent

    val currentDate: LocalDate
        get() = currentLocalDateSubject.value
    val currentDateStr: LiveData<String> = currentLocalDateSubject.mapLatest {
        it.format(DateTimeFormatter.ofPattern("yyyy/MM/dd (E)"))
    }.asLiveData()

    private val _showInputBottomSheet = MutableLiveData<Event<Praise>>()
    val showInputBottomSheet: LiveData<Event<Praise>> = _showInputBottomSheet.distinctUntilChanged()

    init {
        viewModelScope.launch {
            // TODO: 設定時刻を可変にしたい
            settingDataStore.isAlarmInitialized.filter { it.not() }.collect {
                // 19時にアラームを設定する。nowが19時以降である場合は、次の日からアラームを作動させる
                val now = Calendar.getInstance()
                val nextAlarmCalendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 19)
                    set(Calendar.MINUTE, 0)
                }

                if (now >= nextAlarmCalendar) {
                    nextAlarmCalendar.add(Calendar.DATE, 1)
                }

                AlarmHelper.createDailyAlarm(getApplication(), 0, nextAlarmCalendar)
                settingDataStore.updateIsAlarmInitialized(true)
            }
        }
    }

    fun changeDate(additionalDate: Int) {
        currentLocalDateSubject.value = now.plusDays(additionalDate.toLong())
    }

    fun changeDate(epochMilli: Long) = viewModelScope.launch {
        val oldDate = currentLocalDateSubject.value
        val newDate = Instant.ofEpochMilli(epochMilli)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        currentLocalDateSubject.value = newDate

        // pagerのPageを同期させる
        val long = ChronoUnit.DAYS.between(oldDate, newDate)
        val intt = long.toInt()
        _changePageEvent.value = Event(intt)
    }

    fun submit(praise: Praise) = viewModelScope.launch {
        praiseRepository.save(praise)
        _successSubmit.value = Unit
    }

    fun showInputBottomSheet(praise: Praise) {
        _showInputBottomSheet.value = Event(praise)
    }
}