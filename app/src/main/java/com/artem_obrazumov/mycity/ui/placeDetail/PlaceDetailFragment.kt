package com.artem_obrazumov.mycity.ui.placeDetail

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.Attachment
import com.artem_obrazumov.mycity.data.models.Place
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.repository.DataRepository
import com.artem_obrazumov.mycity.databinding.FragmentPlaceDetailBinding
import com.artem_obrazumov.mycity.ui.adapters.AdapterInterfaces
import com.artem_obrazumov.mycity.ui.adapters.GalleryAdapter
import com.artem_obrazumov.mycity.ui.adapters.GalleryAdapter.Companion.MODE_HORIZONTAL_VIDEO
import com.artem_obrazumov.mycity.ui.adapters.ReviewsAdapter
import com.artem_obrazumov.mycity.ui.base.ViewModelFactory
import com.artem_obrazumov.mycity.ui.display.ImageDisplayActivity
import com.artem_obrazumov.mycity.ui.display.VideoDisplayActivity
import com.artem_obrazumov.mycity.ui.reviewEditor.ReviewEditorActivity
import com.artem_obrazumov.mycity.utils.initializeBackPress
import com.artem_obrazumov.mycity.utils.isLogged
import com.artem_obrazumov.mycity.utils.setActionBarTitle
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class PlaceDetailFragment : Fragment() {
    private lateinit var galleryAdapter: GalleryAdapter
    private lateinit var reviewsAdapter: ReviewsAdapter
    private lateinit var viewModel: PlaceDetailViewModel
    private lateinit var binding: FragmentPlaceDetailBinding
    private lateinit var placeId: String
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,
            ViewModelFactory(dataRepository = DataRepository()))
            .get(PlaceDetailViewModel::class.java)
        if (!viewModel.initialized) {
            placeId = requireArguments().getString("placeId")!!
            viewModel.getPlaceData(placeId)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        binding = FragmentPlaceDetailBinding.inflate(inflater)
        val root = binding.root
        binding.swipeRefreshLayout.setOnRefreshListener {
            val bundle = Bundle()
            bundle.putString("placeId", requireArguments().getString("placeId")!!)
            Navigation.findNavController(root).navigate(R.id.refresh, bundle)
        }
        getPlaceData()
        initializeFavoritesButton()
        return root
    }

    override fun onStart() {
        super.onStart()
        initializeBackPress()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        requireView().findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }

    private fun getPlaceData() {
        viewModel.placeData.observe(viewLifecycleOwner, { placeData ->
            binding.main.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
            displayPlaceData(placeData)
        })
    }

    private fun initializeFavoritesButton() {
        binding.favorite.setOnClickListener { view ->
            if (!isFavorite) {
                viewModel.saveToFavorites(requireContext(), placeId)
                ImageViewCompat.setImageTintList(view as ImageView, ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.red_heart)
                ))
                isFavorite = true
                Toast.makeText(
                    context, getString(R.string.add_to_favorites), Toast.LENGTH_SHORT
                ).show()
            } else {
                viewModel.removeFromFavorites(requireContext(), placeId)
                ImageViewCompat.setImageTintList(view as ImageView, ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.placeholder)
                ))
                isFavorite = false
                Toast.makeText(
                    context, getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun displayPlaceData(place: Place) {
        setActionBarTitle(place.title)
        binding.placeName.text = place.title
        binding.placeRating.rating = place.commonRating
        binding.trueRating.text = "(${place.commonRating})"
        binding.placeDescription.text = place.description
        binding.placeAddress.text = place.address
        checkIfFavorite(place.id)

        try {
            val firstAttachment = place.attachment[0]
            if (firstAttachment.type != Attachment.ATTACHMENT_PHOTO) {
                throw IllegalArgumentException("First Attachment is not a photo, skip!")
            }
            Glide.with(requireContext()).load(firstAttachment.link)
                .placeholder(R.drawable.default_user_profile_icon).into(binding.backgroundBanner)
        } catch (ignored : Exception) {}

        initializeGallery(place)
        initializeReviews()
    }

    private fun checkIfFavorite(placeId: String) {
        val favorites = requireContext().getSharedPreferences("user_data", Context.MODE_PRIVATE)
            .getStringSet("favoritesIds", mutableSetOf<String>())
        if (favorites!!.contains(placeId)) {
            isFavorite = true
            ImageViewCompat.setImageTintList(binding.favorite, ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.red_heart)
            ))
        }
    }

    private fun initializeGallery(place: Place) {
        galleryAdapter = GalleryAdapter(
            dataSet = place.attachment, viewType = MODE_HORIZONTAL_VIDEO
        )
        galleryAdapter.listener = object: AdapterInterfaces.GalleryAdapterEventListener {
            override fun onItemClicked(attachment: Attachment) {
                val intent = if (attachment.type == Attachment.ATTACHMENT_PHOTO) {
                    Intent(requireContext(), ImageDisplayActivity::class.java)
                } else {
                    Intent(requireContext(), VideoDisplayActivity::class.java)
                }
                intent.putExtra("URL", attachment.link)
                startActivity(intent)
            }
        }

        binding.gallery.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false)
        binding.gallery.adapter = galleryAdapter
        if (place.attachment.size == 0) {
            binding.gallery.visibility = View.GONE
        }
    }

    private fun initializeReviews() {
        binding.addReviewContainer.setOnClickListener {
            if (FirebaseAuth.getInstance().isLogged()) {
                val intent = Intent(requireContext(), ReviewEditorActivity::class.java)
                intent.putExtra("placeId", placeId)
                startActivity(intent)
            } else {
                Toast.makeText(
                    context, getString(R.string.log_to_review), Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.reviews.observe(viewLifecycleOwner, { reviews ->
            reviewsAdapter = ReviewsAdapter()
            reviewsAdapter.setDataSet(reviews as ArrayList<Review>)
            reviewsAdapter.listener = object: AdapterInterfaces.ReviewsAdapterEventListener {
                override fun onReviewLiked(review: Review) {
                    viewModel.onReviewLiked(review)
                }
            }
            binding.progressBarReviews.visibility = View.GONE
            binding.reviews.layoutManager = LinearLayoutManager(context)
            binding.reviews.adapter = reviewsAdapter
        })
    }
}