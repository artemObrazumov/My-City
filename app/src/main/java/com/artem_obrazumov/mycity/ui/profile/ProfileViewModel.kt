package com.artem_obrazumov.mycity.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class ProfileViewModel(private val repository: DataRepository) : ViewModel() {
    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    var initialized = false

    fun getUserData(userId: String) {
        viewModelScope.launch {
            _userData.value = repository.getUserData(userId)
            onDataReceived()
        }
    }

    private fun onDataReceived() {
        initialized = true
    }
}