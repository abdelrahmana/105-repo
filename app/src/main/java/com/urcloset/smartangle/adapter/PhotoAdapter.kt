package com.urcloset.smartangle.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.demo105.listeners.OnPhotoListener
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.AddPhotoModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener

class PhotoAdapter() :
    ListAdapter<AddPhotoModel, PhotoAdapter.PhotoHolder>(object : DiffUtil.ItemCallback<AddPhotoModel>() {
        override fun areItemsTheSame(oldItem:AddPhotoModel, newItem: AddPhotoModel): Boolean {
            return newItem.photo == newItem.photo

        }

        override fun areContentsTheSame(
            oldItem: AddPhotoModel,
            newItem: AddPhotoModel
        ): Boolean {
            return newItem.photo == newItem.photo
        }
    }) {
    var photoListener: ItemClickListener? = null
    var photoCancel: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        return PhotoHolder(view)
    }



    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        if(getItem(position).img!=0)
            holder.cancel.visibility = View.GONE
        else holder.cancel.visibility = View.VISIBLE

        if(getItem(position).photo!=null) {
            holder.image.setImageURI(getItem(position).photo)
        }

        else{
            holder.image.setImageResource(R.drawable.dr_add_photo)

        }
        holder.image.setOnClickListener {
            if(getItem(position).img!=0)
            photoListener!!.onClick(position)

        }
        holder.cancel.setOnClickListener {
            photoCancel!!.onClick(holder.adapterPosition)
        }



    }

    fun setOnPhotoListener(onPhotoListener: ItemClickListener) {
        photoListener = onPhotoListener
    }
    fun setOnPhotoCancel(onPhotoListener: ItemClickListener) {
        photoCancel = onPhotoListener
    }


    class PhotoHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         val image = itemView.findViewById<ImageView>(R.id.iv_photo)
         val cancel = itemView.findViewById<ImageView>(R.id.iv_cancel)




     }


}