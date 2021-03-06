package com.artem_obrazumov.mycity.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.artem_obrazumov.mycity.data.repository.AuthenticationRepository
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.data.repository.StorageRepository
import com.artem_obrazumov.mycity.ui.authorization.AuthorizationViewModel
import com.artem_obrazumov.mycity.ui.citySelect.CitySelectViewModel
import com.artem_obrazumov.mycity.ui.favorites.FavoritesViewModel
import com.artem_obrazumov.mycity.ui.home.HomeViewModel
import com.artem_obrazumov.mycity.ui.instructions.InstructionsViewModel
import com.artem_obrazumov.mycity.ui.placeDetail.PlaceDetailViewModel
import com.artem_obrazumov.mycity.ui.profile.ProfileViewModel
import com.artem_obrazumov.mycity.ui.profileEdit.ProfileEditViewModel
import com.artem_obrazumov.mycity.ui.registration.RegistrationViewModel
import com.artem_obrazumov.mycity.ui.reviewEditor.ReviewEditorViewModel
import com.artem_obrazumov.mycity.ui.showMore.showMorePlaces.ShowMorePlacesViewModel
import com.artem_obrazumov.mycity.ui.showMore.showMoreUsers.ShowMoreUsersViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val dataRepository: DataRepository? = null,
    private val authRepository: AuthenticationRepository? = null,
    private val storageRepository: StorageRepository? = null
    ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(dataRepository!!) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                return ProfileViewModel(dataRepository!!, storageRepository!!) as T
            }
            modelClass.isAssignableFrom(ProfileEditViewModel::class.java) -> {
                return ProfileEditViewModel(dataRepository!!) as T
            }
            modelClass.isAssignableFrom(AuthorizationViewModel::class.java) -> {
                return AuthorizationViewModel(authRepository!!) as T
            }
            modelClass.isAssignableFrom(CitySelectViewModel::class.java) -> {
                return CitySelectViewModel(dataRepository!!) as T
            }
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) -> {
                return RegistrationViewModel(dataRepository!!, authRepository!!) as T
            }
            modelClass.isAssignableFrom(InstructionsViewModel::class.java) -> {
                return InstructionsViewModel(dataRepository!!) as T
            }
            modelClass.isAssignableFrom(ShowMoreUsersViewModel::class.java) -> {
                return ShowMoreUsersViewModel(dataRepository!!) as T
            }
            modelClass.isAssignableFrom(ShowMorePlacesViewModel::class.java) -> {
                return ShowMorePlacesViewModel(dataRepository!!) as T
            }
            modelClass.isAssignableFrom(PlaceDetailViewModel::class.java) -> {
                return PlaceDetailViewModel(dataRepository!!) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                return FavoritesViewModel(dataRepository!!) as T
            }
            modelClass.isAssignableFrom(ReviewEditorViewModel::class.java) -> {
                return ReviewEditorViewModel(dataRepository!!) as T
            }
        }
        throw IllegalArgumentException("Unknown class name")
    }
}