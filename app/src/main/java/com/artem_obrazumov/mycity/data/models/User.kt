package com.artem_obrazumov.mycity.data.models

// Класс пользователя
data class User(
    var authId: String = "",
    var avatar: String = "",
    var name: String = "",
    var email: String = "",
    var cityName : String = "",
    var nickName: String = "",
    var about: String = "",
    var reviews: Int = 0,
    var rating: Int = 0) {

    companion object {
        fun getStatus(rating : Int) : String {
            return "User"
        }
    }

    fun getFirstName() = name.split(" ")[0]
    fun getSurname()   = name.split(" ")[1]
}