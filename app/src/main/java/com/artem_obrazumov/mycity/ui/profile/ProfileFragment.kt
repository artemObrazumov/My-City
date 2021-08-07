package com.artem_obrazumov.mycity.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.databinding.FragmentProfileBinding
import com.artem_obrazumov.mycity.utils.initializeBackPress
import com.artem_obrazumov.mycity.utils.isLogged
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.utils.setActionBarTitle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.lang.Exception


@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var menu: Menu
    private lateinit var userId: String
    private lateinit var userData: User

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository()))
            .get(ProfileViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        binding = FragmentProfileBinding.inflate(layoutInflater)
        val root = binding.root
        binding.swipeRefreshLayout.setOnRefreshListener {
            val bundle = Bundle()
            bundle.putString("userId", userId)
            Navigation.findNavController(root).navigate(R.id.refresh, bundle)
        }
        return root
    }

    override fun onStart() {
        super.onStart()
        userId = try {
            requireArguments().getString("userId")!!
        } catch (e: Exception) {
            if (auth.isLogged()) auth.currentUser?.uid!! else ""
        }
        initializeBackPress()
        checkIfLogged()
    }

    override fun onResume() {
        super.onResume()
        if (!auth.isLogged() && userId.isEmpty()) {
            Navigation.findNavController(view as View).popBackStack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(R.menu.profile_menu, menu)
        setupMenu()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_profile -> Navigation.findNavController(view as View)
                .navigate(R.id.navigation_edit_profile)
            R.id.leave_profile -> startSignOutDialog()
            else -> requireView().findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun checkIfLogged() {
        if (!auth.isLogged() && userId.isEmpty()) {
            Navigation.findNavController(view as View).navigate(R.id.navigation_unlogged_profile)
        } else {
            initializeViewModel()
            getUserData()
        }
    }

    private fun initializeViewModel() {
        if (!viewModel.initialized) {
            viewModel.getUserData(userId)
        }
    }

    // Getting user data from database
    private fun getUserData() {
        if (userId.isEmpty()) {
            userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        }

        viewModel.userData.observe(viewLifecycleOwner, Observer { userData ->
            this.userData = userData!!
            binding.progressBar.visibility = View.GONE
            binding.main.visibility = View.VISIBLE
            setupMenu()
            displayUserData(userData)
        })
    }

    // Displaying received user data
    private fun displayUserData(userData: User) {
        if (auth.currentUser!!.uid != userData.authId) {
            setActionBarTitle(userData.nickName)
        } else {
            setActionBarTitle(getString(R.string.menu_my_profile))
        }
    }

    private fun setupMenu() {
        try {
            val visitingSelfAccount =
                auth.currentUser!!.uid == userId

            if (visitingSelfAccount) {
                menu.findItem(R.id.leave_profile).isVisible = true
                menu.findItem(R.id.edit_profile).isVisible = true
            }
        } catch (ignored: Exception) {}
    }

    private fun startSignOutDialog() {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.warning))
            .setMessage(getString(R.string.want_leave_account))
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                logOut()
            }
            .setNegativeButton(getString(R.string.cancel)) {
                    dialog, _ ->  dialog.cancel()
            }
            .create().show()
    }

    private fun logOut() {
        auth.signOut()
        requireView().findNavController().popBackStack()
    }
}