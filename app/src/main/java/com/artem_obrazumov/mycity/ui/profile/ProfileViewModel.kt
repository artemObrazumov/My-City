package com.artem_obrazumov.mycity.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.UserModel
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ProfileViewModel(private val repository: DataRepository) : ViewModel() {
    private val _userData = MutableLiveData<UserModel?>()
    var userData: LiveData<UserModel?> = _userData

    fun getData(userId: String) {
        viewModelScope.launch {
            repository.getUserData(userId).collect {
                _userData.value = it
            }
        }
    }
}