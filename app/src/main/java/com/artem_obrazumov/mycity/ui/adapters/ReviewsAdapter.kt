package com.artem_obrazumov.mycity.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.utils.GlideApp
import com.artem_obrazumov.mycity.utils.onClick

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {
    lateinit var listener: AdapterInterfaces.ReviewsAdapterEventListener
    private var dataSet: ArrayList<Review> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSet(usersList: ArrayList<Review>) {
        this.dataSet = usersList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userAvatar: ImageView = view.findViewById(R.id.review_author_avatar)
        val userName: TextView = view.findViewById(R.id.review_author)
        val reviewRating: RatingBar = view.findViewById(R.id.review_rating)
        val reviewContent: TextView = view.findViewById(R.id.review_content)
        val attchmentsContainer: HorizontalScrollView = view.findViewById(R.id.attachments_container)
        val likesCounter: TextView = view.findViewById(R.id.review_likes)
        val likeButton: ImageView = view.findViewById(R.id.thumbup)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.review_row, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentReview: Review = dataSet[position]
        val reviewAuthor = currentReview.author!!
        viewHolder.userName.text = reviewAuthor.nickName
        viewHolder.reviewRating.rating = currentReview.rating.toFloat()
        viewHolder.reviewContent.text = currentReview.content
        viewHolder.likesCounter.text = currentReview.likes.toString()
        viewHolder.likeButton.setOnClickListener {
            listener.onReviewLiked(currentReview)
        }

        try {
            val context : Context = viewHolder.userAvatar.context
            GlideApp.with(context).load(reviewAuthor.avatar)
                .placeholder(R.drawable.default_user_profile_icon).into(viewHolder.userAvatar)
        } catch (ignored : Exception) {}

        if(currentReview.attachments.size == 0) {
            viewHolder.attchmentsContainer.visibility = View.GONE
        }
    }

    override fun getItemCount() = dataSet.size

}