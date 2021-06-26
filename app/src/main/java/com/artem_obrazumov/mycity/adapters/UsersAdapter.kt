package com.artem_obrazumov.mycity.adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artem_obrazumov.mycity.GlideApp
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.models.UserModel
import com.artem_obrazumov.mycity.onClick

class UsersAdapter(): RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    // Listener
    lateinit var listener: AdapterInterfaces.UsersAdapterEventListener

    // Dataset
    private var dataSet: ArrayList<UserModel> = ArrayList()

    fun setDataSet(usersList: ArrayList<UserModel>) {
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

        return ViewHolder(view).onClick { position ->
            val userId = dataSet[position].authId
            listener.onUserClicked(userId)
        }
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentUser: UserModel = dataSet[position]
        viewHolder.userName.text = currentUser.nickName
        viewHolder.userStatus.text = UserModel.getStatus(currentUser.rating)
        viewHolder.userRating.text = currentUser.rating.toString()

        try {
            val context : Context = viewHolder.userAvatar.context
            GlideApp.with(context).load(currentUser.avatar)
                .placeholder(R.drawable.default_user_profile_icon).into(viewHolder.userAvatar)
        } catch (ignored : Exception) {}
    }

    override fun getItemCount() = dataSet.size

}