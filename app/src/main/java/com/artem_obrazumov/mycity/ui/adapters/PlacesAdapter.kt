package com.artem_obrazumov.mycity.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.Attachment
import com.artem_obrazumov.mycity.data.models.Place
import com.bumptech.glide.Glide
import java.lang.Exception
import java.lang.IllegalArgumentException

class PlacesAdapter(): RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {
    lateinit var listener: AdapterInterfaces.PlacesAdapterEventListener
    private var dataSet: ArrayList<Place> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataSet(placesList: ArrayList<Place>) {
        this.dataSet = placesList
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeImage : ImageView = view.findViewById(R.id.place_image)
        val placeTitle : TextView = view.findViewById(R.id.place_title)
        val placeDescription : TextView = view.findViewById(R.id.place_description)
        val placeRating : RatingBar = view.findViewById(R.id.place_rating)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.place_row, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentPlace : Place = dataSet[position]
        viewHolder.placeTitle.text = currentPlace.title
        viewHolder.placeDescription.text = currentPlace.description
        viewHolder.placeRating.rating = currentPlace.ratingScore.toFloat()

        try {
            val context : Context = viewHolder.placeImage.context
            val firstAttachment = currentPlace.attachment[0]
            if (firstAttachment.type != Attachment.ATTACHMENT_PHOTO) {
                throw IllegalArgumentException("First Attachment is not a photo, skip!")
            }

            Glide.with(context).load(firstAttachment.link)
                .placeholder(R.drawable.default_user_profile_icon).into(viewHolder.placeImage)
        } catch (ignored : Exception) {}
    }

    override fun getItemCount() = dataSet.size

}