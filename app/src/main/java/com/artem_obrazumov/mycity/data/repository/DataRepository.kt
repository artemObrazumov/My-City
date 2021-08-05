package com.artem_obrazumov.mycity.data.repository

import com.artem_obrazumov.mycity.data.api.Api
import com.artem_obrazumov.mycity.data.models.UserModel
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

    suspend fun saveUserdataToDatabase(user: UserModel) { Api.saveUserdataToDatabase(user) }
}