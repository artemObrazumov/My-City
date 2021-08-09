package com.artem_obrazumov.mycity.ui.favorites

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class FavoritesViewModel(private val repository: DataRepository) : ViewModel() {
    var initialized = false

    private val _favoritePlacesList = MutableLiveData<MutableList<Place>>()
    val favoritePlacesList: LiveData<MutableList<Place>> = _favoritePlacesList

    fun getFavorites(context: Context) {
        val favoritesIds = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
            .getStringSet("favoritesIds", mutableSetOf<String>())
        viewModelScope.launch {
            _favoritePlacesList.value = repository.getFavoritePlaces(favoritesIds as Set<String>)
            initialized = true
        }
    }
}