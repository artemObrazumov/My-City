package com.artem_obrazumov.mycity.ui.placeDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class PlaceDetailViewModel(private val repository: DataRepository) : ViewModel() {
    var initialized: Boolean = false

    private val _placeData = MutableLiveData<Place>()
    val placeData: LiveData<Place> = _placeData

    fun getPlaceData(placeId: String) {
        viewModelScope.launch {
            _placeData.value = repository.getPlaceData(placeId)
            onDataReceived()
        }
    }

    private fun onDataReceived() {
        initialized = true
    }
}