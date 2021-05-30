package com.artem_obrazumov.mycity.models

import com.google.firebase.database.IgnoreExtraProperties

// Класс пользователя
@IgnoreExtraProperties
data class UserModel(
    var id: String = "",
    var authId: String = "",
    var avatar: String = "",
    var name: String = "",
    var nickName: String = "",
    var rating: Int = 0) {

    companion object {
        fun getStatus(rating : Int) : String {
            return "User"
        }
    }
}