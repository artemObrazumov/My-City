package com.artem_obrazumov.mycity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class HomeViewModel(private val repository: DataRepository) : ViewModel() {
    var cityName: String = "0"
    var initialized: Boolean = false

    private val _criticsList = MutableLiveData<MutableList<User>>()
    val criticsList: LiveData<MutableList<User>> = _criticsList

    private val _placesList = MutableLiveData<MutableList<Place>>()
    val placesList: LiveData<MutableList<Place>> = _placesList

    fun getData() {
        viewModelScope.launch {
            _placesList.value = repository.getPopularPlaces(cityName)
            _criticsList.value = repository.getPopularCritics(cityName)

            onDataReceived()
        }
    }

    private fun onDataReceived() {
        initialized = true
    }
}