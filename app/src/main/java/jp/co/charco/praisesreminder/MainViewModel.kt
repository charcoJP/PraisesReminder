package jp.co.charco.praisesreminder

import androidx.lifecycle.*
import jp.co.charco.praisesreminder.data.db.entity.Praise
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    // TODO: あとでDIに変更する
    private val praiseDao by lazy { App.database.praiseDao() }

    private val _praisesInput = MutableLiveData<String>()
    val praisesInput: LiveData<String> = _praisesInput

    private val _enableSubmitButton = MutableLiveData<Boolean>()
    val enableSubmitButton: LiveData<Boolean> = _enableSubmitButton

    private val _successSubmit = MutableLiveData<Unit>()
    val successSubmit: LiveData<Unit> = _successSubmit

    val savedPraisesTexts = praiseDao.getAll().map { list -> list.map { it.content } }.asLiveData()

    fun praisesInputChanged(input: String) {
        _praisesInput.value = input
        _enableSubmitButton.value = isLoginValid(input)
    }

    fun submit() = viewModelScope.launch {
        if (_enableSubmitButton.value == false) throw IllegalStateException("Input is invalid.")

        praiseDao.save(Praise(content = praisesInput.value ?: ""))
        _praisesInput.value = ""
        _successSubmit.value = Unit
    }

    private fun isLoginValid(input: String): Boolean {
        return input.isNotBlank()
    }
}