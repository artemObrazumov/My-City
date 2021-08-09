package com.artem_obrazumov.mycity.data.repository

import android.net.Uri
import com.artem_obrazumov.mycity.data.api.Api
import kotlinx.coroutines.ExperimentalCoroutinesApi

/*
 * Repository to use storage
*/
@ExperimentalCoroutinesApi
class StorageRepository {
    suspend fun eraseAvatar(userId: String) = Api.eraseAvatar(userId)
    suspend fun changeAvatar(userId: String, newAvatarURI: Uri) =
        Api.changeAvatar(userId, newAvatarURI)
}