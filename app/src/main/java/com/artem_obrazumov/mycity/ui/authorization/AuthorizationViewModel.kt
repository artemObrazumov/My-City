package com.artem_obrazumov.mycity.ui.authorization

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.repository.AuthenticationRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class AuthorizationViewModel (private val repository: AuthenticationRepository) : ViewModel() {
    private val _authorizationResult = MutableLiveData<Task<AuthResult>>()
    val authorizationResult: LiveData<Task<AuthResult>> = _authorizationResult

    fun authorizeUser(email: String, password: String) {
        viewModelScope.launch {
            _authorizationResult.value = repository.authorizeUser(email, password)
        }
    }
}