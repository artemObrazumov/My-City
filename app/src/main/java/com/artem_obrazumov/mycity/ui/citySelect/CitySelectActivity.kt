package com.artem_obrazumov.mycity.ui.citySelect

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.databinding.ActivityCitySelectBinding
import com.artem_obrazumov.mycity.ui.authorization.AuthorizationViewModel
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.ui.main.MainActivity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import android.app.Activity
import android.widget.Toast
import com.artem_obrazumov.mycity.R

@ExperimentalCoroutinesApi
class CitySelectActivity : AppCompatActivity() {
    private lateinit var viewModel: CitySelectViewModel
    private lateinit var binding: ActivityCitySelectBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var citiesList: ArrayList<String>
    private lateinit var selectedCity: String

    override fun onCreate(savedInstanceState: Bundle?) {
        database = FirebaseDatabase.getInstance()
        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository()))
            .get(CitySelectViewModel::class.java)

        super.onCreate(savedInstanceState)
        binding = ActivityCitySelectBinding.inflate(layoutInflater)
        val root = binding.root
        setContentView(root)
        binding.selectButton.setOnClickListener{
            if (!checkIfCitySelected()) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.select_city),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            startDisappearAnimation()
            if (callingActivity != null) {
                returnDataForResult()
                return@setOnClickListener
            }
            saveData()

            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(
                    Intent(this@CitySelectActivity, MainActivity::class.java)
                )
            }, 1000)
        }

        getCitiesList()
    }


    private fun checkIfCitySelected(): Boolean {
        return this::selectedCity.isInitialized
    }

    @Suppress("UNCHECKED_CAST")
    private fun getCitiesList() {
        viewModel.citiesList.observe(this, Observer { citiesList ->
            this.citiesList = citiesList as ArrayList<String>
            startAppearAnimation()
            setupDialog()
        })
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun startAppearAnimation() {
        binding.selectCityText.animate()
            .translationY(20f).alpha(1f).setDuration(800L)
        binding.selectedCityName.animate()
            .translationY(20f).alpha(1f).setDuration(800L)
        binding.selectButton.animate()
            .translationY(20f).alpha(1f).setDuration(800L)
    }

    private fun setupDialog() {
        binding.selectedCityName.setOnClickListener {
            AlertDialog.Builder(this)
                .setItems(citiesList.toTypedArray()
                ) { _, which ->
                    selectedCity = citiesList[which]
                    binding.selectedCityName.text = selectedCity
                }
                .create().show()
        }
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun startDisappearAnimation() {
        binding.selectedCityName.setOnClickListener(null)
        binding.selectButton.setOnClickListener(null)

        binding.selectCityText.animate()
            .translationY(40f).alpha(0f).setDuration(800L)
        binding.selectedCityName.animate()
            .translationY(40f).alpha(0f).setDuration(800L)
        binding.selectButton.animate()
            .translationY(40f).alpha(0f).setDuration(800L)
    }

    private fun saveData() {
        with (getSharedPreferences("user_data", Context.MODE_PRIVATE).edit()) {
            putString("cityName", selectedCity)
            apply()
        }
    }

    private fun returnDataForResult() {
        val result = Intent()
        result.putExtra("city", selectedCity)
        setResult(RESULT_OK, result)
        finish()
    }
}