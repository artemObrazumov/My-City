package com.artem_obrazumov.mycity.ui.profileEdit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.databinding.FragmentProfileBinding
import com.artem_obrazumov.mycity.utils.initializeBackPress
import com.artem_obrazumov.mycity.utils.isLogged
import com.artem_obrazumov.mycity.data.models.UserModel
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.databinding.FragmentEditProfileBinding
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.utils.setActionBarTitle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class ProfileEditFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        requireView().findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }
}