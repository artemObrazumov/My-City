package com.artem_obrazumov.mycity.data.repository

import com.artem_obrazumov.mycity.data.api.Api
import com.artem_obrazumov.mycity.data.api.ServerApi
import com.artem_obrazumov.mycity.data.models.PlaceModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

/*
* Repository to handle API authorization requests
*/
@ExperimentalCoroutinesApi
class AuthorizationRepository {
    fun registerUser(email: String, password: String) = Api.registerUser(email, password)
    fun authorizeUser(email: String, password: String) = Api.authorizeUser(email, password)
}