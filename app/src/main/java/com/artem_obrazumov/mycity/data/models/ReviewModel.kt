package com.artem_obrazumov.mycity.data.models

data class ReviewModel(
    var id : String = "",
    var authorId : String = "",
    var placeId : String = "",
    var photoAttachments : ArrayList<String>,
    var videoAttachments : ArrayList<String>
)