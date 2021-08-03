package com.artem_obrazumov.mycity.ui.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.databinding.ActivityRegistrationBinding
import com.artem_obrazumov.mycity.utils.getUserCity
import com.artem_obrazumov.mycity.data.models.UserModel
import com.artem_obrazumov.mycity.utils.getTrimmedText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {

    // ViewModel
    private lateinit var viewModel: RegistrationViewModel

    // Binding
    private lateinit var binding: ActivityRegistrationBinding

    // User object
    private val user: UserModel = UserModel()

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    // Local variables
    private var isFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegistrationViewModel::class.java)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRegistrationForm()
        binding.registerButton.setOnClickListener {
            requestUserRegistration()
        }
    }

    // Animations
    @Suppress("UsePropertyAccessSyntax")
    private fun showRegistrationForm() {
        binding.registrationContent.animate()
            .translationY(0f).alpha(1f).setDuration(500L)
        binding.loadingContent.animate()
            .translationY(-40f).alpha(0f).setDuration(500L)
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun showLoading() {
        binding.registrationContent.animate()
            .translationY(-20f).alpha(0f).setDuration(500L)
        binding.loadingContent.animate()
            .translationY(0f).alpha(1f).setDuration(500L)
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun showFinishedDialog() {
        binding.loadingContent.animate()
            .translationY(40f).alpha(0f).setDuration(500L)
        binding.finishingRegistrationContent.animate()
            .translationY(-40f).alpha(1f).setDuration(500L)
    }

    // Trying to register user
    private fun requestUserRegistration() {
        if (isValid()) {
            showLoading()
            fillUserFields(user)
            viewModel.registerUser(user.email, binding.inputPassword.getTrimmedText())
        }
    }

    private fun isValid() : Boolean {
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
        // Comparing passwords
        if (binding.inputPassword.getTrimmedText() != binding.inputPasswordRepeat.getTrimmedText()) {
            binding.inputPassword.error = getString(R.string.short_nickname)
            binding.inputPassword.requestFocus()
            return false
        }
        return true
    }

    // Filling fields in user object
    private fun fillUserFields(user: UserModel) {
        user.name = "${binding.inputName.getTrimmedText()} ${binding.inputSurname.getTrimmedText()}"
        user.nickName = binding.inputNickname.getTrimmedText()
        user.email = binding.inputEmail.getTrimmedText()
        user.cityName = getUserCity(applicationContext)
    }

    // Processing task result to invoke some action
    private fun manageRegistrationTaskResult(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            user.authId = auth.currentUser?.uid.toString()
            viewModel.saveUserdataToDatabase(database, user)
            onUserdataSaved()
        } else {
            task.exception?.printStackTrace()
            showRegistrationForm()
        }
    }

    // Finishing registration
    private fun onUserdataSaved() {
        isFinished = true
        showFinishedDialog()
        binding.restartButton.setOnClickListener {
            finishAndRemoveTask()
        }
    }

    override fun onBackPressed() {
        // Going back only if registration isn't finished
        if (!isFinished) {
            super.onBackPressed()
        }
    }
}