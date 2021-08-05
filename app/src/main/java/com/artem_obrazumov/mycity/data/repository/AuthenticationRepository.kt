package com.artem_obrazumov.mycity.data.repository

import com.artem_obrazumov.mycity.data.api.Api
import kotlinx.coroutines.ExperimentalCoroutinesApi

/*
* Repository to handle API authorization requests
*/
@ExperimentalCoroutinesApi
class AuthenticationRepository {
    suspend fun registerUser(email: String, password: String) = Api.registerUser(email, password)
    suspend fun authorizeUser(email: String, password: String) = Api.authorizeUser(email, password)
}