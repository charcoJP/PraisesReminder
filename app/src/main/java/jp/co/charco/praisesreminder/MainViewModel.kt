package jp.co.charco.praisesreminder

import androidx.lifecycle.*
import jp.co.charco.praisesreminder.data.db.entity.Praise
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // TODO: あとでDIに変更する
    private val praiseDao by lazy { App.database.praiseDao() }

    private val _successSubmit = MutableLiveData<Unit>()
    val successSubmit: LiveData<Unit> = _successSubmit

    val savedPraises = praiseDao.getAll().asLiveData()

    fun submit(input: String) = viewModelScope.launch {
        praiseDao.save(Praise(content = input))
        _successSubmit.value = Unit
    }

    fun delete(praise: Praise) = viewModelScope.launch {
        praiseDao.delete(praise)
    }
}