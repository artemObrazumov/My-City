package com.artem_obrazumov.mycity.ui.instructions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.ui.instructions.models.InstructionsScript
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class InstructionsViewModel(private val repository: DataRepository) : ViewModel() {
    private val _instructionsScript = MutableLiveData<InstructionsScript>()
    val instructionsScript: LiveData<InstructionsScript> = _instructionsScript

    var initialized = false

    fun getInstructionsScript() {
        viewModelScope.launch {
            _instructionsScript.value = repository.getInstructionScript()
            onDataReceived()
        }
    }

    private fun onDataReceived() {
        initialized = true
    }
}
