package com.artem_obrazumov.mycity.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.AuthenticationRepository
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class RegistrationViewModel(
    private val dataRepository: DataRepository = DataRepository(),
    private val authenticationRepository: AuthenticationRepository = AuthenticationRepository()
) : ViewModel() {
    private val _registrationResult = MutableLiveData<Task<AuthResult>>()
    val registrationResult: LiveData<Task<AuthResult>> = _registrationResult

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            _registrationResult.value = authenticationRepository.registerUser(email, password)
        }
    }

    fun saveUserdataToDatabase(user: User) {
        viewModelScope.launch {
            dataRepository.saveUserdataToDatabase(user)
        }
    }
}