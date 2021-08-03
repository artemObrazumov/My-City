package com.artem_obrazumov.mycity.ui.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.UserModel
import com.artem_obrazumov.mycity.data.repository.AuthorizationRepository
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class RegistrationViewModel(
    private val dataRepository: DataRepository = DataRepository(),
    private val authorizationRepository: AuthorizationRepository = AuthorizationRepository()
) : ViewModel() {

    fun registerUser(email: String, password: String) {
        viewModelScope.launch {
            authorizationRepository.registerUser(email, password)
        }
    }

    fun saveUserdataToDatabase(user: UserModel) {
        viewModelScope.launch {
            dataRepository.saveUserdataToDatabase(user)
        }
    }
}