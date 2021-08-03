package com.artem_obrazumov.mycity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.PlaceModel
import com.artem_obrazumov.mycity.data.models.UserModel
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(private val repository: DataRepository) : ViewModel() {
    var cityName : String = "0"
    var initialized : Boolean = false

    private val _criticsList = MutableLiveData<MutableList<UserModel?>>()
    val criticsList: LiveData<MutableList<UserModel?>> = _criticsList

    private val _placesList = MutableLiveData<MutableList<PlaceModel?>>()
    val placesList: LiveData<MutableList<PlaceModel?>> = _placesList

    fun getData() {
        viewModelScope.launch {
            repository.getPopularPlaces(cityName).collect { placesList ->
                _placesList.value = placesList
            }

            repository.getPopularCritics(cityName).collect { criticsList ->
                _criticsList.value = criticsList
            }

            onDataReceived()
        }
    }

    private fun onDataReceived() {
        initialized = true
    }
}