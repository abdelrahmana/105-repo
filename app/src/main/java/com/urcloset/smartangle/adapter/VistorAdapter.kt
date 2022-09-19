package com.urcloset.smartangle.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.sellerActivity.SellerActivity
import com.urcloset.smartangle.model.NotificationModel
import com.urcloset.smartangle.model.VisitorModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener

class VisitorAdapter(): RecyclerView.Adapter<VisitorAdapter.ViewHolder>() {
    private var visitors:ArrayList<VisitorModel.Data.Users.Data>?=null
    private var context: Context?=null
    constructor(context: Context, items:ArrayList<VisitorModel.Data.Users.Data>) : this() {

        this.context=context
        this.visitors=items

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.vistor_item, parent, false)

        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val visitorData: VisitorModel.Data.Users.Data = visitors?.get(position)!!
        visitorData.visitor?.let {
            val vistor: VisitorModel.Data.Users.Data.Visitor = visitorData.visitor
            if (!vistor.image.isNullOrEmpty()) {
                Glide.with(holder.itemView.context.applicationContext)
                    .load(BasicTools.getUrlHttpImg(holder.itemView.context, vistor.image))
                    .circleCrop()
                    .into(holder.profileImg)


            }


            holder.tvName.text = visitorData.visitor.name
            holder.time.text = visitorData.visitedAt

        }
        holder.rootVisitor.setOnClickListener {
            val gson = Gson()
            val intent = Intent(holder.itemView.context, SellerActivity::class.java)
            intent.putExtra("identifier", gson.toJson(visitorData.visitor?.id))
            holder.itemView.context.startActivity(intent)

        }


    }
  fun  addItem(item:VisitorModel.Data.Users.Data){
      visitors?.add(item)
      notifyItemInserted(visitors?.size!!-1)

  }




    class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        val profileImg: ImageView =itemView.findViewById<ImageView>(R.id.iv_profile)
        val tvName: TextView =itemView.findViewById<TextView>(R.id.tv_name)
        val time: TextView =itemView.findViewById<TextView>(R.id.time)
        val rootVisitor = itemView.findViewById<RelativeLayout>(R.id.root_visitor)



    }

    override fun getItemCount(): Int {
       return visitors?.size!!
    }
}