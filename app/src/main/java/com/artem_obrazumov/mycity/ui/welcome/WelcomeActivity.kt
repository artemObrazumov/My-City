package com.artem_obrazumov.mycity.ui.welcome

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.databinding.ActivityWelcomeBinding
import com.artem_obrazumov.mycity.ui.citySelect.CitySelectActivity
import com.artem_obrazumov.mycity.ui.main.MainActivity
import com.artem_obrazumov.mycity.ui.welcome.WelcomeActivity.WelcomeScreenConstants.WELCOME_SCREEN_DURATION
import com.artem_obrazumov.mycity.utils.getUserCity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.util.Util
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class WelcomeActivity : AppCompatActivity() {
    object WelcomeScreenConstants {
        const val WELCOME_SCREEN_DURATION : Long = 5000L
    }

    private lateinit var binding : ActivityWelcomeBinding
    private lateinit var database : FirebaseFirestore
    private var isAnimationFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        database = FirebaseFirestore.getInstance()
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        setupAnimations()
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun setupAnimations() {
        binding.welcomeText.animate()
            .translationY(20f).alpha(1f).setDuration(800L)

        binding.treesBgLayer1.animate()
            .translationY(0f).setDuration(1250L)
            .setInterpolator(AccelerateDecelerateInterpolator())

        binding.treesBgLayer2.animate()
            .translationY(0f).setDuration(1500L)
            .setInterpolator(AccelerateDecelerateInterpolator())

        binding.cloudsBgLayer1.animate()
            .translationY(0f).setDuration(1000L)
            .setInterpolator(AccelerateDecelerateInterpolator())

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            onAnimationFinished()
        }, WELCOME_SCREEN_DURATION)
    }

    private fun onAnimationFinished() {
        isAnimationFinished = true
        leaveActivity()
    }

    private fun leaveActivity() {
        val currentCityName: String = getUserCity(applicationContext)

        database.collection("Cities")
            .document(currentCityName).get()
            .addOnSuccessListener{ snapshot ->
            val intent: Intent = if (!snapshot.getBoolean("exists")!!) {
                Intent(applicationContext, CitySelectActivity::class.java)
            } else {
                Intent(applicationContext, MainActivity::class.java)
                // TODO: mainAct
            }
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (isAnimationFinished) {
            leaveActivity()
        }
    }
}