package com.artem_obrazumov.mycity.ui.profile

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.data.repository.StorageRepository
import com.artem_obrazumov.mycity.databinding.FragmentProfileBinding
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.utils.GlideApp
import com.artem_obrazumov.mycity.utils.initializeBackPress
import com.artem_obrazumov.mycity.utils.isLogged
import com.artem_obrazumov.mycity.utils.setActionBarTitle
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {
    private val ERASE_AVATAR = 0
    private val CHANGE_AVATAR = 1

    private val getNewAvatar = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val croppedImageURI = CropImage
                .getActivityResult(result.data!!).uri
            requestChangeAvatar(croppedImageURI)
        } else if (result.resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
        }
    }

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
            ViewModelFactory(
                dataRepository = DataRepository(),
                storageRepository = StorageRepository()
            ))
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
    private fun displayUserData(user: User) {
        if (auth.currentUser?.uid != user.authId) {
            setActionBarTitle(user.nickName)
        } else {
            setActionBarTitle(getString(R.string.menu_my_profile))
            binding.avatar.setOnClickListener {
                startAvatarDialog()
            }
        }
        binding.userName.text = user.name
        binding.userNickname.text = user.nickName
        binding.userAbout.text = user.about
        binding.userCity.text = user.cityName
        binding.userRating.text = user.rating.toString()
        if (user.about.isEmpty()) {
            binding.userAbout.text = getString(R.string.no_user_about)
        }
        loadAvatar(user.avatar)
    }

    private fun loadAvatar(url: String) {
        try {
            GlideApp.with(requireContext()).load(url)
                .placeholder(R.drawable.default_user_profile_icon).into(binding.avatar)
        } catch (ignored : Exception) {}
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

    private fun startAvatarDialog() {
        AlertDialog.Builder(context)
            .setItems(
                arrayOf(
                    getString(R.string.erase_avatar),
                    getString(R.string.change_avatar)
                )
            ) { _, which ->
                if (which == ERASE_AVATAR) requestEraseAvatar()
                else {
                    val intent = CropImage.activity(null)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setAspectRatio(1, 1)
                        .getIntent(requireContext())
                    getNewAvatar.launch(intent)
                }
            }
            .create().show()
    }

    private fun requestEraseAvatar() {
        viewModel.eraseAvatar(auth.currentUser!!.uid)
        viewModel.newAvatarURL.observe(viewLifecycleOwner, { newUrL ->
            if(newUrL == "error") {
                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
            loadAvatar(newUrL)
        })
    }

    private fun requestChangeAvatar(newAvatarUri: Uri) {
        viewModel.changeAvatar(auth.currentUser!!.uid, newAvatarUri)
        viewModel.newAvatarURL.observe(viewLifecycleOwner, { newUrL ->
            if(newUrL == "error") {
                Toast.makeText(context, getString(R.string.error), Toast.LENGTH_SHORT).show()
            }
            loadAvatar(newUrL)
        })
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