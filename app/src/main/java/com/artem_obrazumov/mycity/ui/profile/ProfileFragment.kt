package com.artem_obrazumov.mycity.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.databinding.FragmentProfileBinding
import com.artem_obrazumov.mycity.initializeBackPress
import com.artem_obrazumov.mycity.isLogged
import com.artem_obrazumov.mycity.models.UserModel
import com.artem_obrazumov.mycity.setActionBarTitle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {

    // ViewModel
    private lateinit var viewModel: ProfileViewModel

    // Binding
    private lateinit var binding: FragmentProfileBinding

    // Firebase
    private lateinit var auth: FirebaseAuth

    // Local variables
    private lateinit var userId: String
    private lateinit var userData: UserModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initializeBackPress()
        checkIfLogged()
    }

    private fun checkIfLogged() {
        auth = FirebaseAuth.getInstance()
        if (!auth.isLogged()) {
            Navigation.findNavController(view as View).navigate(R.id.navigation_unlogged_profile)
        } else {
            getUserData()
        }
    }

    // Getting user data from database
    private fun getUserData() {
        userId = arguments?.getString("userId").toString()
        viewModel.getData(userId)
        viewModel.userData.observe(viewLifecycleOwner, Observer { userData ->
            this.userData = userData!!
            displayUserData(userData)
        })
    }

    // Displaying received user data
    private fun displayUserData(userData: UserModel) {
        if (auth.uid != userData.authId) {
            setActionBarTitle(userData.nickName)
        } else {
            setActionBarTitle(getString(R.string.menu_my_profile))
        }
    }

    override fun onResume() {
        super.onResume()
        if (!auth.isLogged()) {
            Navigation.findNavController(view as View).popBackStack()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        requireView().findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }
}