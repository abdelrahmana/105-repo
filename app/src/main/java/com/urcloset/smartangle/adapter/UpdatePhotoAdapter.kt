package com.urcloset.smartangle.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.AddPhotoModel
import com.urcloset.smartangle.model.UpdatePhotoModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener

class UpdatePhotoAdapter() :
    ListAdapter<UpdatePhotoModel, UpdatePhotoAdapter.PhotoHolder>(object : DiffUtil.ItemCallback<UpdatePhotoModel>() {
        override fun areItemsTheSame(oldItem: UpdatePhotoModel, newItem: UpdatePhotoModel): Boolean {
            return newItem.photo == newItem.photo

        }

        override fun areContentsTheSame(
            oldItem: UpdatePhotoModel,
            newItem: UpdatePhotoModel
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
        else if(getItem(position).imgUrl!=null) {
            BasicTools.loadImage(BasicTools.getUrlHttpImg(holder.itemView.context,getItem(position).imgUrl!!)  ,holder.image,   object : DownloadListener {
                override fun completed(status: Boolean, bitmap: Bitmap) {
                    val anim =
                        AnimationUtils.loadAnimation(
                            holder.itemView.context,
                            android.R.anim.fade_in
                        )
                    anim!!.setDuration(1000)
                    holder.image.animation = anim
                }
            })
        }

        else{
            holder.image.setImageResource(R.drawable.dr_add_photo)

        }
        if(position==currentList.size-1)
            holder.image.setOnClickListener {
                photoListener!!.onClick(position)

            }
        holder.cancel.setOnClickListener {
            photoCancel!!.onClick(position)
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