package com.artem_obrazumov.mycity.ui.instructions

import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.databinding.ActivityInstructionsBinding
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.ui.instructions.adapters.InstructionsPagerAdapter
import com.artem_obrazumov.mycity.ui.main.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class InstructionsActivity : AppCompatActivity() {
    private lateinit var viewModel: InstructionsViewModel
    private lateinit var binding: ActivityInstructionsBinding
    private lateinit var adapter: InstructionsPagerAdapter

    private val animationSpeed = 300L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructionsBinding.inflate(layoutInflater)
        val root = binding.root
        initializeViewModel()
        initializeViewPager()
        setContentView(root)

        binding.finishButton.setOnClickListener {
            with (
                getSharedPreferences("user_data", AppCompatActivity.MODE_PRIVATE).edit()
            ) {
                putBoolean("watchedInstruction", true)
                apply()
            }
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
        binding.skipButton.setOnClickListener {
            with (
                getSharedPreferences("user_data", AppCompatActivity.MODE_PRIVATE).edit()
            ) {
                putBoolean("watchedInstruction", true)
                apply()
            }
            startActivity(
                Intent(this, MainActivity::class.java)
            )
        }
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository())
        ).get(InstructionsViewModel::class.java)

        if (!viewModel.initialized) {
            viewModel.getInstructionsScript()
        }
    }

    private fun initializeViewPager() {
        viewModel.instructionsScript.observe(this, { script ->
            adapter = InstructionsPagerAdapter(script, applicationContext)
            binding.viewPager.adapter = adapter
            binding.viewPager.setPadding(80, 0, 80, 0)
            binding.viewPager.addOnPageChangeListener(
                object: ViewPager.OnPageChangeListener {
                    override fun onPageScrolled(
                        position: Int,
                        positionOffset: Float,
                        positionOffsetPixels: Int
                    ) {}

                    override fun onPageSelected(position: Int) {
                        if (position == adapter.count-1) {
                            onLastPageSelected()
                        } else {
                            onLastPageNotSelected()
                        }
                    }

                    override fun onPageScrollStateChanged(state: Int) {}

                })
        })
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun onLastPageSelected() {
        binding.finishButton.animate()
            .translationY(0f).setDuration(animationSpeed)
            .setInterpolator(AccelerateDecelerateInterpolator())

        binding.skipButton.animate()
            .translationY(86f).setDuration(animationSpeed)
            .setInterpolator(AccelerateDecelerateInterpolator())
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun onLastPageNotSelected() {
        binding.finishButton.animate()
            .translationY(86f).setDuration(animationSpeed)
            .setInterpolator(AccelerateDecelerateInterpolator())

        binding.skipButton.animate()
            .translationY(0f).setDuration(animationSpeed)
            .setInterpolator(AccelerateDecelerateInterpolator())
    }
}