package com.artem_obrazumov.mycity.data.repository

import com.artem_obrazumov.mycity.data.api.Api
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
    suspend fun getCitiesList() = Api.getCitiesList()
    suspend fun getInstructionScript() = Api.getInstructionScript()

    suspend fun saveUserdataToDatabase(user: User) { Api.saveUserdataToDatabase(user) }
}