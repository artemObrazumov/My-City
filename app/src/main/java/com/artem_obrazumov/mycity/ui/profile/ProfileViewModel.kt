package com.artem_obrazumov.mycity.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ProfileViewModel : ViewModel() {
    private val _userData = MutableLiveData<UserModel?>()
    var userData: LiveData<UserModel?> = _userData

    private fun getUserData(userId: String) : Flow<UserModel?> {
        val reference = FirebaseDatabase.getInstance().getReference("Users/${userId}")

        return callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val user = dataSnapshot.getValue(UserModel::class.java)
                    offer(user)
                    close()
                }

                override fun onCancelled(error: DatabaseError) {}
            }
            reference.addListenerForSingleValueEvent(listener)

            awaitClose{}
        }
    }

    fun getData(userId: String) {
        viewModelScope.launch {
            getUserData(userId).collect {
                _userData.value = it
            }
        }
    }
}