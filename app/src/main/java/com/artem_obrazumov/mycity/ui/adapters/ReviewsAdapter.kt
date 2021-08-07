package com.artem_obrazumov.mycity.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.Review
import com.artem_obrazumov.mycity.data.models.User
import com.artem_obrazumov.mycity.utils.GlideApp
import com.artem_obrazumov.mycity.utils.onClick

class ReviewsAdapter(): RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {
    lateinit var listener: AdapterInterfaces.ReviewsAdapterEventListener
    private var dataSet: ArrayList<Review> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSet(usersList: ArrayList<Review>) {
        this.dataSet = usersList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userAvatar: ImageView = view.findViewById(R.id.user_avatar)
        val userName: TextView = view.findViewById(R.id.user_name)
        val userStatus: TextView = view.findViewById(R.id.user_status)
        val userRating: TextView = view.findViewById(R.id.user_rating)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.user_list_row, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentUser: Review = dataSet[position]
//        viewHolder.userName.text = currentUser.nickName
//        viewHolder.userStatus.text = User.getStatus(currentUser.rating)
//        viewHolder.userRating.text = currentUser.rating.toString()
//
//        try {
//            val context : Context = viewHolder.userAvatar.context
//            GlideApp.with(context).load(currentUser.avatar)
//                .placeholder(R.drawable.default_user_profile_icon).into(viewHolder.userAvatar)
//        } catch (ignored : Exception) {}
    }

    override fun getItemCount() = dataSet.size

}