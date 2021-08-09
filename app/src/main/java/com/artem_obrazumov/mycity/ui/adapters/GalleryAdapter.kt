package com.artem_obrazumov.mycity.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.artem_obrazumov.mycity.R
import com.artem_obrazumov.mycity.data.models.Attachment
import com.artem_obrazumov.mycity.data.models.Attachment.Companion.ATTACHMENT_PHOTO
import com.artem_obrazumov.mycity.utils.onClick
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.util.*

class GalleryAdapter(
    private var dataSet: ArrayList<Attachment> = arrayListOf(),
    private var viewType: Int = MODE_FULL
) : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    companion object {
        const val MODE_FULL = 1 // Полные изображения
        const val MODE_CROPPED = 2 // Обрезанные изображения
        const val MODE_HORIZONTAL = 3 // Изображения в ряд
        const val MODE_HORIZONTAL_VIDEO = 4 // Видео в ряд
    }

    lateinit var listener: AdapterInterfaces.GalleryAdapterEventListener

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var attachment: Attachment? = null
        var image: ImageView = view.findViewById(R.id.image)
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = if (viewType == MODE_FULL) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.image_row, parent, false)
        } else if (this.viewType == MODE_CROPPED) {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cropped_image_row, parent, false)
        } else {
            LayoutInflater.from(parent.context)
                .inflate(R.layout.image_horizontal_row, parent, false)
        }
        return ViewHolder(view).onClick { position ->
            listener.onItemClicked(dataSet[position])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.attachment = dataSet[position]
        loadImage(holder, position)
    }

    private fun loadImage(holder: ViewHolder, position: Int) {
        if (holder.attachment!!.type == ATTACHMENT_PHOTO) {
            try {
                if (getItemViewType(position) == MODE_CROPPED) {
                    Glide.with(holder.image).load(holder.attachment!!.link)
                        .apply(RequestOptions().override(900, 900))
                        .centerCrop().placeholder(R.color.placeholder).into(holder.image)
                } else {
                    Glide.with(holder.image).load(holder.attachment!!.link)
                        .placeholder(R.color.placeholder)
                        .into(holder.image)
                }
            } catch (ignored: Exception) {}
        } else {
            Glide.with(holder.image).asBitmap()
                .load(holder.attachment!!.link)
                .apply(RequestOptions().frame(1000000L))
                .apply(RequestOptions().override(900, 900))
                .into(holder.image)
        }
    }

    override fun getItemCount(): Int =
        try {
            dataSet.size
        } catch (e: Exception) { 0 }
}