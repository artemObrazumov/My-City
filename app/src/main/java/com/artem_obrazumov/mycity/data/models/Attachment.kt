package com.artem_obrazumov.mycity.data.models

data class Attachment(
    var type : Int = ATTACHMENT_PHOTO,
    var link : String = ""
) {
    companion object {
        const val ATTACHMENT_PHOTO = 0
        const val ATTACHMENT_VIDEO = 1
    }
}