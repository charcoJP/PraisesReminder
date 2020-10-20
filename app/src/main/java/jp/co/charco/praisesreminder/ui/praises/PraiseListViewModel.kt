package jp.co.charco.praisesreminder.ui.praises

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import jp.co.charco.praisesreminder.data.db.entity.Praise
import jp.co.charco.praisesreminder.data.repository.PraiseRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

@FlowPreview
@ExperimentalCoroutinesApi
class PraiseListViewModel @ViewModelInject constructor(
    private val praiseRepository: PraiseRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val additionalDate: Long
        get() = savedStateHandle.get<Long>(PraiseListFragment.KEY_ADDITIONAL_DATE) ?: 0

    private val currentLocalDateChannel = ConflatedBroadcastChannel(LocalDate.now().plusDays(additionalDate))

    val savedPraises = currentLocalDateChannel.asFlow().flatMapLatest  {
        praiseRepository.getPraises(it)
    }.asLiveData()

    fun reloadPraises() = viewModelScope.launch {
        currentLocalDateChannel.send(currentLocalDateChannel.value)
    }

    fun moveItem(visibleList: MutableList<Praise>) = viewModelScope.launch {
        praiseRepository.updateOrders(visibleList)
    }

    fun delete(praise: Praise) = viewModelScope.launch {
        praiseRepository.delete(praise)
        reloadPraises()
    }
}