package com.artem_obrazumov.mycity.ui.reviewEditor

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.databinding.ActivityReviewEditorBinding
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.ui.showMore.showMorePlaces.ShowMorePlacesViewModel
import com.artem_obrazumov.mycity.utils.getTrimmedText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.util.Util
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ReviewEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReviewEditorBinding
    private lateinit var viewModel: ReviewEditorViewModel
    private lateinit var placeId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository())
        ).get(ReviewEditorViewModel::class.java)

        binding = ActivityReviewEditorBinding.inflate(layoutInflater)
        placeId = intent!!.getStringExtra("placeId")!!
        setContentView(binding.root)
        startAppearAnimation()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.editing_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.apply -> requestUpload()
            else -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("RestrictedApi")
    private fun requestUpload() {
        if (valid()) {
            val review = Review(
                Util.autoId(),
                FirebaseAuth.getInstance().currentUser!!.uid,
                placeId, binding.inputReview.getTrimmedText(),
                binding.rating.rating.toInt()
            )
            viewModel.uploadReview(review)
            finish()
        }
    }

    private fun valid(): Boolean {
        if (binding.rating.rating == 0f) {
            Toast.makeText(
                applicationContext, getString(R.string.no_rating), Toast.LENGTH_SHORT
            ).show()
            return false
        }
        if (binding.inputReview.getTrimmedText().length > 850) {
            Toast.makeText(
                applicationContext, getString(R.string.too_long_review), Toast.LENGTH_SHORT
            ).show()
            return false
        }
        return true
    }

    @Suppress("UsePropertyAccessSyntax")
    private fun startAppearAnimation() {
        binding.main.animate()
            .translationY(0f).alpha(1f).setDuration(500L)
    }
}