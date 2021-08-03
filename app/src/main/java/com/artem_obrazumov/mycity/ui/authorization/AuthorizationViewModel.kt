package com.artem_obrazumov.mycity.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.repository.AuthorizationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class AuthorizationViewModel (private val repository: AuthorizationRepository) : ViewModel() {
    fun authorizeUser(email: String, password: String) {
        viewModelScope.launch {
            repository.registerUser(email, password)
        }
    }
}