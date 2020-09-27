package jp.co.charco.praisesreminder

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _praisesInput = MutableLiveData<String>()
    val praisesInput: LiveData<String> = _praisesInput

    private val _enableSubmitButton = MutableLiveData<Boolean>()
    val enableSubmitButton: LiveData<Boolean> = _enableSubmitButton

    private val _successSubmit = MutableLiveData<Unit>()
    val successSubmit: LiveData<Unit> = _successSubmit

    val savedPraisesTexts = MutableLiveData("")

    fun praisesInputChanged(input: String) {
        _praisesInput.value = input
        _enableSubmitButton.value = isLoginValid(input)
    }

    fun submit() {
        if (_enableSubmitButton.value == false) throw IllegalStateException("Input is invalid.")

        // TODO: ひとまずStringにため込む
        savedPraisesTexts.value += praisesInput.value + "\n"
        _praisesInput.value = ""
        _successSubmit.value = Unit
    }

    private fun isLoginValid(input: String): Boolean {
        return input.isNotBlank()
    }
}