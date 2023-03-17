package com.clint.nasa.pictures

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.clint.nasa.R
import com.clint.nasa.core.extensions.inflate
import com.clint.nasa.core.extensions.loadFromUrl
import javax.inject.Inject
import kotlin.properties.Delegates

class PicturesAdapter @Inject constructor() : RecyclerView.Adapter<PicturesAdapter.ViewHolder>() {

    internal var picturesList: List<Pictures> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nasaImageView: ImageView = itemView.findViewById(R.id.nasaImageView)
        val textViewDescription: TextView = itemView.findViewById(R.id.textViewDescription)
        val textViewDate: TextView = itemView.findViewById(R.id.textViewDate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.row_pictures))

    override fun getItemCount(): Int = picturesList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pictures = picturesList[position]
        holder.nasaImageView.loadFromUrl(pictures.url)
        holder.textViewDescription.text = pictures.title
        holder.textViewDate.text = pictures.date
    }
}