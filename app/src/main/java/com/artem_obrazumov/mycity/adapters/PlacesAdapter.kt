package com.artem_obrazumov.mycity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.models.PlaceModel
import com.artem_obrazumov.mycity.models.UserModel
import com.bumptech.glide.Glide
import java.lang.Exception

class PlacesAdapter(): RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private var dataSet: ArrayList<PlaceModel> = ArrayList()

    fun setDataSet(placesList: ArrayList<PlaceModel>) {
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
        val currentPlace : PlaceModel = dataSet[position]
        viewHolder.placeTitle.text = currentPlace.title
        viewHolder.placeDescription.text = currentPlace.description
        viewHolder.placeRating.rating = currentPlace.rating.toFloat()

        try {
            val context : Context = viewHolder.placeImage.context
            Glide.with(context).load(currentPlace.photos[0])
                .placeholder(R.drawable.default_user_profile_icon).into(viewHolder.placeImage)
        } catch (ignored : Exception) {}
    }

    override fun getItemCount() = dataSet.size

}