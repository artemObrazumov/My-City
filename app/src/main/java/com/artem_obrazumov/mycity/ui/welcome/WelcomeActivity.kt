package com.artem_obrazumov.mycity.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.artem_obrazumov.mycity.databinding.ActivityWelcomeBinding
import com.artem_obrazumov.mycity.ui.citySelect.CitySelectActivity
import com.artem_obrazumov.mycity.ui.instructions.InstructionsActivity
import com.artem_obrazumov.mycity.ui.welcome.WelcomeActivity.WelcomeScreenConstants.WELCOME_SCREEN_DURATION
import com.artem_obrazumov.mycity.utils.getUserCity
import com.google.firebase.database.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class WelcomeActivity : AppCompatActivity() {
    object WelcomeScreenConstants {
        const val WELCOME_SCREEN_DURATION : Long = 6000L
    }

    private lateinit var binding : ActivityWelcomeBinding
    private lateinit var database : FirebaseDatabase
    private var isAnimationFinished = false

    override fun onCreate(savedInstanceState: Bundle?) {
        database = FirebaseDatabase.getInstance()
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

        database.getReference("Cities/$currentCityName").addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val intent : Intent = if (!snapshot.exists()) {
                        Intent(applicationContext, CitySelectActivity::class.java)
                    } else {
                        Intent(applicationContext, InstructionsActivity::class.java)
                        // TODO: mainAct
                    }
                    startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {}
            }
        )
    }

    override fun onResume() {
        super.onResume()
        if (isAnimationFinished) {
            leaveActivity()
        }
    }
}