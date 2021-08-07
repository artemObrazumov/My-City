package com.artem_obrazumov.mycity.ui.showMore.showMoreUsers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ShowMoreUsersViewModel(private val repository: DataRepository) : ViewModel() {
    var cityName: String = "0"
    var initialized: Boolean = false

    private val _criticsList = MutableLiveData<MutableList<User>>()
    val criticsList: LiveData<MutableList<User>> = _criticsList

    fun getData() {
        viewModelScope.launch {
            _criticsList.value = repository.getPopularCritics(cityName)
            onDataReceived()
        }
    }

    private fun onDataReceived() {
        initialized = true
    }
}