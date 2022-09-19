package com.urcloset.smartangle.adapter

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.sellerActivity.SellerActivity
import com.urcloset.smartangle.model.UsersModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener
import com.urcloset.smartangle.tools.TemplateActivity


class UserAdapter():ListAdapter<UsersModel.Data.SellerUser,UserAdapter.ViewHolder>(object : DiffUtil.ItemCallback<UsersModel.Data.SellerUser>() {

    override fun areItemsTheSame(oldItem: UsersModel.Data.SellerUser, newItem: UsersModel.Data.SellerUser): Boolean {
        return oldItem.id ==newItem.id

    }

    override fun areContentsTheSame(
        oldItem: UsersModel.Data.SellerUser,
        newItem: UsersModel.Data.SellerUser
    ): Boolean {
        return oldItem.name == newItem.name
    }
}) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val sellerProfile : UsersModel.Data.SellerUser =getItem(position)
        try {

            var x=BasicTools.getUrlImg(holder.itemView.context,sellerProfile.image.toString())

           // Log.i("TEST_TEST","$x")


          if (getItem(position).image != "") {
                getItem(position).image?.let {
                    Glide.with(holder.itemView.context.applicationContext).load(BasicTools.getUrlHttpImg(holder.itemView.context,it))
                        .circleCrop()
                        .into(holder.ivUserImage)

                }
            }
        }
        catch (e:Exception){

        }
        holder.tvTitle.text = getItem(position).name
        holder.buttonVisit.setOnClickListener{view->
                val gson = Gson()
                val intent = Intent(holder.itemView.context, SellerActivity::class.java)
                intent.putExtra("identifier", gson.toJson(sellerProfile))
                holder.itemView.context.startActivity(intent)

        }

    }
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val ivUserImage = itemView.findViewById<ImageView>(R.id.iv_user_img)
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val buttonVisit = itemView.findViewById<CardView>(R.id.visit)

    }
}