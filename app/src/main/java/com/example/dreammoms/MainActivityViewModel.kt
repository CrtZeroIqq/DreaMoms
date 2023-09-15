package com.example.dreammoms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivityViewModel : ViewModel() {

    private val openAIRepository = OpenAIRepository("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")

    fun getGPTResponse(userMessage: String, onSuccess: (String) -> Unit, onError: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = openAIRepository.getGPTResponse(userMessage)
                withContext(Dispatchers.Main) {
                    onSuccess(response.trim())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onError()
                }
            }
        }
    }
}
