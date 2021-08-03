package com.artem_obrazumov.mycity.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.databinding.FragmentUnloggedProfileBinding
import com.artem_obrazumov.mycity.utils.isLogged
import com.artem_obrazumov.mycity.ui.authorization.AuthorizationActivity
import com.artem_obrazumov.mycity.ui.registration.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth


class UnloggedProfileFragment : Fragment() {
    // Binding
    private lateinit var binding : FragmentUnloggedProfileBinding

    // ActionBar
    private lateinit var actionBar : ActionBar

    // Firebase
    private lateinit var auth : FirebaseAuth

    @SuppressLint("RestrictedApi")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        actionBar = (activity as AppCompatActivity?)!!.supportActionBar!!
        actionBar.setShowHideAnimationEnabled(false)
        actionBar.hide()

        binding = FragmentUnloggedProfileBinding.inflate(inflater)
        val root = binding.root
        animateAppearAnimation()
        setupButtons()
        return root
    }

    override fun onStart() {
        super.onStart()
        checkIfLogged()
    }

    private fun checkIfLogged() {
        auth = FirebaseAuth.getInstance()
        if (auth.isLogged()) {
            Navigation.findNavController(view as View).navigate(R.id.navigation_my_profile)
        }
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun animateAppearAnimation() {
        binding.content.animate()
            .translationY(0f).alpha(1f).setDuration(1000L)
    }

    private fun setupButtons() {
        binding.loginButton.setOnClickListener{
            activity?.let{ activity ->
                val intent = Intent (activity, AuthorizationActivity::class.java)
                activity.startActivity(intent)
            }
        }

        binding.registerButton.setOnClickListener{
            activity?.let{ activity ->
                val intent = Intent (activity, RegistrationActivity::class.java)
                activity.startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        actionBar.show()
    }
}