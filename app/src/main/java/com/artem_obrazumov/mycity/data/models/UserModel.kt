package com.artem_obrazumov.mycity.data.models

// Класс пользователя
data class UserModel(
    var authId: String = "",
    var avatar: String = "",
    var name: String = "",
    var email: String = "",
    var cityName : String = "",
    var nickName: String = "",
    var rating: Int = 0) {

    companion object {
        fun getStatus(rating : Int) : String {
            return "User"
        }
    }
}