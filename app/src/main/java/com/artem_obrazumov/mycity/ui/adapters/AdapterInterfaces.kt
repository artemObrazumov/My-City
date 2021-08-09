package com.artem_obrazumov.mycity.ui.adapters

import com.artem_obrazumov.mycity.data.models.Attachment
import com.artem_obrazumov.mycity.data.models.Review

class AdapterInterfaces {
    interface UsersAdapterEventListener {
        fun onUserClicked(userId: String)
    }
    interface PlacesAdapterEventListener {
        fun onPlaceClicked(placeId: String)
    }
    interface ReviewsAdapterEventListener {
        fun onReviewLiked(review: Review)
    }
    interface GalleryAdapterEventListener {
        fun onItemClicked(attachment: Attachment)
    }
}