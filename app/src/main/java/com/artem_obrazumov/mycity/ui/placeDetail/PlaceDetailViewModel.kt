package com.artem_obrazumov.mycity.ui.placeDetail

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
class PlaceDetailViewModel(private val repository: DataRepository) : ViewModel() {
    var initialized: Boolean = false

    private val _placeData = MutableLiveData<Place>()
    val placeData: LiveData<Place> = _placeData

    private val _reviews = MutableLiveData<MutableList<Review>>()
    val reviews: LiveData<MutableList<Review>> = _reviews

    fun getPlaceData(placeId: String) {
        viewModelScope.launch {
            _placeData.value = repository.getPlaceData(placeId)
            _reviews.value = repository.getReviews(placeId)
            onDataReceived()
        }
    }

    private fun onDataReceived() {
        initialized = true
    }

    fun saveToFavorites(context: Context, placeId: String) {
        repository.savePlaceToFavorites(context, placeId)
    }

    fun removeFromFavorites(context: Context, placeId: String) {
        repository.removePlaceFromFavorites(context, placeId)
    }

    fun onReviewLiked(review: Review) {
        viewModelScope.launch {
            repository.changeUserRating(review)
        }
    }
}