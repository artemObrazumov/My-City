package com.artem_obrazumov.mycity.data.api

import android.content.Context
import android.net.Uri
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
                           loadAuthorData: Boolean) : MutableList<Review>
    suspend fun getCitiesList(): MutableList<String>
    suspend fun getInstructionScript(): InstructionsScript
    suspend fun getFavoritePlaces(ids: Set<String>): MutableList<Place>

    suspend fun saveUserdataToDatabase(user: User)
    fun savePlaceToFavorites(context: Context, placeId: String)
    fun removePlaceFromFavorites(context: Context, placeId: String)
    fun uploadReview(review: Review)
    suspend fun changeRating(review: Review)
    suspend fun changeUserRating(review: Review)

    // Authorization requests
    suspend fun registerUser(
        email: String,
        password: String
    ) : Task<AuthResult>
    suspend fun authorizeUser(
        email: String,
        password: String
    ) : Task<AuthResult>

    // Storage requests
    suspend fun eraseAvatar(userId: String): String
    suspend fun changeAvatar(userId: String, newAvatarURI: Uri): String
}