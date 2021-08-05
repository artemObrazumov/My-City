package com.artem_obrazumov.mycity.data.api

import com.artem_obrazumov.mycity.data.models.PlaceModel
import com.artem_obrazumov.mycity.data.models.UserModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.database.DataSnapshot
import kotlinx.coroutines.flow.Flow

interface ApiService {
    // Database requests
    suspend fun getPopularCritics(cityName: String) : MutableList<UserModel>
    suspend fun getPopularPlaces(cityName: String) : MutableList<PlaceModel>
    suspend fun getUserData(userId: String) : UserModel
    suspend fun getCitiesList() : MutableList<String>

    suspend fun saveUserdataToDatabase(user: UserModel)

    // Authorization requests
    suspend fun registerUser(
        email: String,
        password: String
    ) : Task<AuthResult>
    suspend fun authorizeUser(
        email: String,
        password: String
    ) : Task<AuthResult>
}