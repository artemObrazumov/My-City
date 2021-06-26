package com.artem_obrazumov.mycity.ui.activities.citySelect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class CitySelectViewModel : ViewModel() {
    private val _citiesList = MutableLiveData<MutableList<String?>>()
    val citiesList: LiveData<MutableList<String?>> = _citiesList

    private fun getCitiesList() : Flow<MutableList<String?>> {
        val reference = FirebaseDatabase.getInstance().getReference("Cities")
        val cities : MutableList<String?> = ArrayList()

        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot : DataSnapshot in dataSnapshot.children) {
                        cities.add(snapshot.key)
                    }
                    offer(cities)
                    close()
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            reference.addListenerForSingleValueEvent(listener)

            awaitClose{}
        }
    }

    private fun getData() {
        viewModelScope.launch {
            getCitiesList().collect { citiesList ->
                _citiesList.value = citiesList
            }
        }
    }

    init {
        getData()
    }
}
