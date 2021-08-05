package com.artem_obrazumov.mycity.ui.citySelect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
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
