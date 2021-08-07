package com.artem_obrazumov.mycity.data.api

import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.ui.instructions.models.InstructionsScript
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult

interface ApiService {
    // Database requests
    suspend fun getPopularCriticsWithLimit(cityName: String) : MutableList<User>
    suspend fun getPopularCritics(cityName: String) : MutableList<User>
    suspend fun getPopularPlaces(cityName: String) : MutableList<Place>
    suspend fun getUserData(userId: String) : User
    suspend fun getPlaceData(placeId: String) : Place
    suspend fun getReviews(placeId: String,
                           loadAuthorData: Boolean = true) : MutableList<Review>
    suspend fun getCitiesList() : MutableList<String>
    suspend fun getInstructionScript(): InstructionsScript

    suspend fun saveUserdataToDatabase(user: User)

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