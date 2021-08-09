package com.artem_obrazumov.mycity.data.repository

import android.content.Context
import com.artem_obrazumov.mycity.data.api.Api
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi

/*
 * Repository to handle API database requests
*/
@ExperimentalCoroutinesApi
class DataRepository {
    suspend fun getPopularCriticsWithLimit(cityName: String) = Api.getPopularCriticsWithLimit(cityName)
    suspend fun getPopularCritics(cityName: String) = Api.getPopularCritics(cityName)
    suspend fun getPopularPlaces(cityName: String) = Api.getPopularPlaces(cityName)
    suspend fun getUserData(userId: String) = Api.getUserData(userId)
    suspend fun getPlaceData(placeId: String)  = Api.getPlaceData(placeId)
    suspend fun getReviews(placeId: String,
                           loadAuthorData: Boolean = true)  = Api.getReviews(placeId, loadAuthorData)
    suspend fun getCitiesList() = Api.getCitiesList()
    suspend fun getInstructionScript() = Api.getInstructionScript()
    suspend fun getFavoritePlaces(ids: Set<String>) = Api.getFavoritePlaces(ids)
    suspend fun changeRating(review: Review) {
        Api.changeRating(review)
    }
    suspend fun changeUserRating(review: Review) {
        Api.changeUserRating(review)
    }

    suspend fun saveUserdataToDatabase(user: User) { Api.saveUserdataToDatabase(user) }
    fun savePlaceToFavorites(context: Context, placeId: String) {
        Api.savePlaceToFavorites(context, placeId)
    }
    fun removePlaceFromFavorites(context: Context, placeId: String) {
        Api.removePlaceFromFavorites(context, placeId)
    }
    fun uploadReview(review: Review) {
        Api.uploadReview(review)
    }
}