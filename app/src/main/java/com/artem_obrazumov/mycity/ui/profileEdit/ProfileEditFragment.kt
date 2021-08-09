package com.artem_obrazumov.mycity.ui.profileEdit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.databinding.FragmentEditProfileBinding
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.ui.citySelect.CitySelectActivity
import com.artem_obrazumov.mycity.utils.getTrimmedText
import com.artem_obrazumov.mycity.utils.initializeBackPress
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class ProfileEditFragment : Fragment() {

    private val getCity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data!!.extras!!
            newCity = data.getString("city")
        }
    }
    private lateinit var viewModel: ProfileEditViewModel
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var auth: FirebaseAuth

    private lateinit var currentUserData: User
    private var newCity: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository()))
            .get(ProfileEditViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        binding = FragmentEditProfileBinding.inflate(layoutInflater)
        binding.changeCityButton.setOnClickListener{
            getNewCity()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initializeBackPress()
        initializeViewModel()
        viewModel.userData.observe(viewLifecycleOwner, { user ->
            currentUserData = user
            displayCurrentUserData()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.editing_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.apply -> tryApplyChanges()
            else -> requireView().findNavController().popBackStack()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeViewModel() {
        if (!viewModel.initialized) {
            viewModel.getUserData(auth.currentUser!!.uid)
        }
    }

    private fun displayCurrentUserData() {
        // Display user data from the received object
        binding.inputName.setText(currentUserData.getFirstName())
        binding.inputSurname.setText(currentUserData.getSurname())
        binding.inputNickname.setText(currentUserData.nickName)
        binding.inputAbout.setText(currentUserData.about)
        startAppearAnimation()
    }

    private fun getNewCity() {
        val intent = Intent(context, CitySelectActivity::class.java)
        getCity.launch(intent)
    }

    private fun tryApplyChanges() {
        if (isValid()) {
            // Trying to apply changes
            val user = getNewUserObject()
            updateCityInSharedPreferences()
            viewModel.saveUserData(user)
            Toast.makeText(context, getString(R.string.data_updated), Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun startAppearAnimation() {
        binding.progressBar.visibility = View.GONE
        binding.main.visibility = View.VISIBLE
        binding.main.animate()
            .translationY(-40f).alpha(1f).setDuration(500L)
    }

    private fun updateCityInSharedPreferences() {
        // Update the city in shared preferences if it was changed
        if (newCity != null) {
            with (
                activity?.getSharedPreferences("user_data", Context.MODE_PRIVATE)!!.edit()
            ) {
                putString("cityName", newCity as String)
                apply()
            }
        }
    }

    private fun getNewUserObject() = currentUserData.copy(
        name = "${binding.inputName.getTrimmedText()} ${binding.inputSurname.getTrimmedText()}",
        nickName = binding.inputNickname.getTrimmedText(),
        about = binding.inputAbout.getTrimmedText()
    )

    private fun isValid(): Boolean {
        // Make sure data in fields isn't too short
        if (binding.inputName.getTrimmedText().length < 3) {
            binding.inputName.error = getString(R.string.short_name)
            binding.inputName.requestFocus()
            return false
        }
        if (binding.inputSurname.getTrimmedText().length < 3) {
            binding.inputSurname.error = getString(R.string.short_surname)
            binding.inputSurname.requestFocus()
            return false
        }
        if (binding.inputNickname.getTrimmedText().length < 3) {
            binding.inputNickname.error = getString(R.string.short_nickname)
            binding.inputNickname.requestFocus()
            return false
        }
        if (binding.inputAbout.getTrimmedText().length > 100) {
            binding.inputAbout.error = getString(R.string.too_much_about)
            binding.inputAbout.requestFocus()
            return false
        }
        return true
    }
}