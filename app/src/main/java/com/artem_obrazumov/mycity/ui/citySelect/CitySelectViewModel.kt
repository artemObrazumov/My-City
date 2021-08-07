package com.artem_obrazumov.mycity.ui.citySelect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class CitySelectViewModel(private val repository: DataRepository) : ViewModel() {
    private val _citiesList = MutableLiveData<MutableList<String>>()
    val citiesList: LiveData<MutableList<String>> = _citiesList

    private fun getData() {
        viewModelScope.launch {
            _citiesList.value = repository.getCitiesList()
        }
    }

    init {
        getData()
    }
}
