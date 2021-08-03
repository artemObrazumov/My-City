package com.artem_obrazumov.mycity.data.api

import com.artem_obrazumov.mycity.data.models.PlaceModel
import com.artem_obrazumov.mycity.data.models.UserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface ApiService {
    // Database requests
    fun getPopularCritics(cityName: String) : Flow<MutableList<UserModel?>>
    fun getPopularPlaces(cityName: String) : Flow<MutableList<PlaceModel?>>
    fun getUserData(userId: String) : Flow<UserModel?>

    fun saveUserdataToDatabase(user: UserModel)

    // Authorization requests
    fun registerUser(
        email: String,
        password: String
    ) : Flow<Task<AuthResult>>
    fun authorizeUser(
        email: String,
        password: String
    ) : Flow<Task<AuthResult>>
}