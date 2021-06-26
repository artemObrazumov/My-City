package com.artem_obrazumov.mycity.ui.activities.authorization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.artem_obrazumov.mycity.databinding.ActivityAuthorizationBinding
import com.artem_obrazumov.mycity.getTrimmedText
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthorizationActivity : AppCompatActivity() {

    // ViewModel
    private lateinit var viewModel: AuthorizationViewModel

    // Binding
    private lateinit var binding: ActivityAuthorizationBinding

    // Firebase
    private lateinit var auth: FirebaseAuth

    // Local variables
    private var isFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthorizationViewModel::class.java)
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
        viewModel.authorizeUser(auth, email, password,
            OnCompleteListener { result -> manageAuthorizationTaskResult(result) })
    }

    // Processing task result to invoke some action
    private fun manageAuthorizationTaskResult(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            onUserAuthorized()
        } else {
            task.exception?.printStackTrace()
            showRegistrationForm()
        }
    }

    // Finishing registration
    private fun onUserAuthorized() {
        isFinished = true
        showFinishedDialog()
        binding.restartButton.setOnClickListener {
            finishAffinity()
        }
    }

    override fun onBackPressed() {
        // Going back only if registration isn't finished
        if (!isFinished) {
            super.onBackPressed()
        }
    }
}