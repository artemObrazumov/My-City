package com.artem_obrazumov.mycity.data.repository

import com.artem_obrazumov.mycity.data.api.Api
import com.artem_obrazumov.mycity.data.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi

/*
 * Repository to handle API database requests
*/
@ExperimentalCoroutinesApi
class DataRepository {
    suspend fun getPopularCritics(cityName: String) = Api.getPopularCritics(cityName)
    suspend fun getPopularPlaces(cityName: String) = Api.getPopularPlaces(cityName)
    suspend fun getUserData(userId: String) = Api.getUserData(userId)
    suspend fun getCitiesList() = Api.getCitiesList()
    suspend fun getInstructionScript() = Api.getInstructionScript()

    suspend fun saveUserdataToDatabase(user: User) { Api.saveUserdataToDatabase(user) }
}