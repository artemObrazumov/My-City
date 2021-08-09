package com.artem_obrazumov.mycity.ui.reviewEditor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class ReviewEditorViewModel(private val repository: DataRepository) : ViewModel() {
    fun uploadReview(review: Review) {
        viewModelScope.launch {
            repository.uploadReview(review)
            repository.changeRating(review)
        }
    }
}
