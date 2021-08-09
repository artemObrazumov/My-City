package com.artem_obrazumov.mycity.ui.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.data.repository.StorageRepository
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class ProfileViewModel(
    private val dataRepository: DataRepository,
    private val storageRepository: StorageRepository
    ) : ViewModel() {

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    private val _newAvatarURL = MutableLiveData<String>()
    val newAvatarURL: LiveData<String> = _newAvatarURL

    var initialized = false

    fun getUserData(userId: String) {
        viewModelScope.launch {
            _userData.value = dataRepository.getUserData(userId)
            onDataReceived()
        }
    }

    fun eraseAvatar(userId: String) {
        viewModelScope.launch {
            storageRepository.eraseAvatar(userId)
        }
    }

    fun changeAvatar(userId: String, newAvatarUri: Uri) {
        viewModelScope.launch {
            storageRepository.changeAvatar(userId, newAvatarUri)
        }
    }

    private fun onDataReceived() {
        initialized = true
    }
}