package com.artem_obrazumov.mycity.ui.profileEdit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.UserModel
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class ProfileViewModel(private val repository: DataRepository) : ViewModel() {
    private val _userData = MutableLiveData<UserModel>()
    var userData: LiveData<UserModel> = _userData

    fun getUserData(userId: String) {
        viewModelScope.launch {
            _userData.value = repository.getUserData(userId)
        }
    }
}