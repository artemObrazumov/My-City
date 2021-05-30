package com.artem_obrazumov.mycity.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.models.PlaceModel
import com.artem_obrazumov.mycity.models.UserModel
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
class HomeViewModel() : ViewModel() {

    @ExperimentalCoroutinesApi
    private fun getPopularCritics(cityID : String) : Flow<MutableList<UserModel?>> {
        val reference = FirebaseDatabase.getInstance().getReference("users")
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
            reference.addListenerForSingleValueEvent(listener)

            awaitClose{}
        }
    }

    private fun getPopularPlaces(cityID : String) : MutableList<PlaceModel> {
        val places : MutableList<PlaceModel> = ArrayList()
        places.add(PlaceModel (title = "1", address = "dfsdfsdfsdf", description = "descfgdfgsdfgdsfgdfg", photos = arrayListOf("one")) )
        places.add(PlaceModel (title = "2", address = "dfsdfsdfsdf", description = "descfgdfgsdfgdsfgdfg", rating = 2f, photos = arrayListOf("one")) )
        return places
    }

    private val _criticsList = MutableLiveData<MutableList<UserModel?>>()

    val criticsList: LiveData<MutableList<UserModel?>> = _criticsList

    private val _placesList = MutableLiveData<MutableList<PlaceModel>>().apply {
        value = getPopularPlaces("0")
    }
    val placesList: LiveData<MutableList<PlaceModel>> = _placesList



    fun getData() {
        viewModelScope.launch {
            getPopularCritics("00").collect { list ->
                _criticsList.value = list
            }
        }
    }

    init {
        getData()
    }
}