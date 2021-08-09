package com.artem_obrazumov.mycity.ui.display

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.artem_obrazumov.mycity.R
import com.bumptech.glide.Glide
import com.artem_obrazumov.mycity.databinding.ActivityImageDisplayBinding
import java.lang.Exception


class ImageDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityImageDisplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadImageFromIntent()
    }

    private fun loadImageFromIntent() {
        try {
            val url = intent.extras!!.getString("URL")
            Glide.with(applicationContext)
                .load(url)
                .into(binding.viewingImage)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}