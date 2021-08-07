package com.artem_obrazumov.mycity.ui.adapters

class AdapterInterfaces {
    interface UsersAdapterEventListener {
        fun onUserClicked(userId: String)
    }
    interface PlacesAdapterEventListener {
        fun onPlaceClicked(placeId: String)
    }
    interface ReviewsAdapterEventListener {

    }
}