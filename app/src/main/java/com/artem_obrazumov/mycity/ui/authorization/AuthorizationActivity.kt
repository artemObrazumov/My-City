package com.artem_obrazumov.mycity.ui.authorization

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.repository.AuthenticationRepository
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.databinding.ActivityAuthorizationBinding
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.ui.profile.ProfileViewModel
import com.artem_obrazumov.mycity.utils.getTrimmedText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AuthorizationActivity : AppCompatActivity() {
    private lateinit var viewModel: AuthorizationViewModel
    private lateinit var binding: ActivityAuthorizationBinding
    private lateinit var auth: FirebaseAuth
    private var isFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            ViewModelFactory(authRepository = AuthenticationRepository()))
            .get(AuthorizationViewModel::class.java)

        auth = FirebaseAuth.getInstance()
        binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showRegistrationForm()
        binding.loginButton.setOnClickListener {
            authorizeUser()
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

    // Trying to authorize user
    private fun authorizeUser() {
        showLoading()
        val email = binding.inputEmail.getTrimmedText()
        val password = binding.inputPassword.getTrimmedText()
        viewModel.authorizeUser(email, password)
        viewModel.authorizationResult.observe(this, Observer { task ->
            manageAuthorizationTaskResult(task)
        })
    }

    // Processing task result to invoke some action
    private fun manageAuthorizationTaskResult(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            onUserAuthorized()
        } else {
            task.exception?.printStackTrace()
            showRegistrationForm()
            Toast.makeText(
                applicationContext,
                getString(R.string.authorization_error),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Finishing authorization
    private fun onUserAuthorized() {
        isFinished = true
        showFinishedDialog()
        binding.restartButton.setOnClickListener {
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        // Going back only if authorization isn't finished
        if (!isFinished) {
            super.onBackPressed()
        }
    }
}