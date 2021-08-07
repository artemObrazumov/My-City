package com.artem_obrazumov.mycity.data.models

// Класс отзыва
data class Review(
    var id: String = "",
    var authorId: String = "",
    var placeId: String = "",
    var attachments: ArrayList<Attachment> = arrayListOf(),
    var author: User? = null)