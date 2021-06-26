package com.artem_obrazumov.mycity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.models.PlaceModel
import com.artem_obrazumov.mycity.models.UserModel
import com.google.firebase.database.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel : ViewModel() {
    var cityName : String = "0"
    var initialized : Boolean = false

    private val _criticsList = MutableLiveData<MutableList<UserModel?>>()
    val criticsList: LiveData<MutableList<UserModel?>> = _criticsList

    private val _placesList = MutableLiveData<MutableList<PlaceModel?>>()
    val placesList: LiveData<MutableList<PlaceModel?>> = _placesList

    private fun getPopularCritics(cityName : String) : Flow<MutableList<UserModel?>> {
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        val query : Query = reference.orderByChild("cityName").equalTo(cityName)
        val critics : MutableList<UserModel?> = ArrayList()

        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot : DataSnapshot in dataSnapshot.children) {
                        val user: UserModel? = snapshot.getValue(UserModel::class.java)
                        critics.add(user)
                    }
                    offer(critics)
                    close()
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            query.addListenerForSingleValueEvent(listener)

            awaitClose{}
        }
    }

    private fun getPopularPlaces(cityName : String) : Flow<MutableList<PlaceModel?>> {
        val reference = FirebaseDatabase.getInstance().getReference("Places")
        val query : Query = reference.orderByChild("cityName").equalTo(cityName)
        val places : MutableList<PlaceModel?> = ArrayList()

        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot : DataSnapshot in dataSnapshot.children) {
                        val place: PlaceModel? = snapshot.getValue(PlaceModel::class.java)
                        places.add(place)
                    }
                    offer(places)
                    close()
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            query.addListenerForSingleValueEvent(listener)

            awaitClose{}
        }
    }

    fun getData() {
        viewModelScope.launch {
            getPopularPlaces(cityName).collect { placesList ->
                _placesList.value = placesList
            }

            getPopularCritics(cityName).collect { criticsList ->
                _criticsList.value = criticsList
            }

            onDataReceived()
        }
    }

    private fun onDataReceived() {
        initialized = true
    }
}