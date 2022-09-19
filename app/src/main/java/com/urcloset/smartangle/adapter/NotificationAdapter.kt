package com.urcloset.smartangle.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.project105.ConditionListAdapter
import com.urcloset.smartangle.fragment.bookmark_fragment.IBookMarkFragment
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.NotificationModel
import com.urcloset.smartangle.model.NotificationModel.Data.NotificationList.NotificationItem
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener

class NotificationAdapter() : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
     var notifications:ArrayList<NotificationItem>?=null
    private var context: Context?=null
    lateinit var itemClickListener :ItemClickListener
    constructor(context:Context, items:ArrayList<NotificationItem>) : this() {

        this.context=context
        this.notifications=items

    }
    fun setOnItemClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.ViewHolder {
        return NotificationAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.notification_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     val notification:NotificationItem =   notifications?.get(position)!!
        if(notification.status==0)
        holder.root.setBackground(context!!.resources.getDrawable(R.drawable.un_seen_notification_bg))
        else  holder.root.setBackground(context!!.resources.getDrawable(R.drawable.seen_notification_bg))

        holder.root.setOnClickListener {
            this.itemClickListener.onClick(position)

        }
        if(notification.item?.itemMedia!=null){
            if(notification.item.itemMedia.isNotEmpty()){
                BasicTools.loadImage(BasicTools.getUrlHttpImg(context!!,
                    notification.item.itemMedia.get(0)?.mediaPath.toString()!!),holder.notificationImg
                    ,object : DownloadListener {
                    override fun completed(status: Boolean, bitmap: Bitmap) {
                       val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                       anim!!.setDuration(1000)
                        holder.notificationImg.animation= anim
                    }
                })



            }
        }
        if(notification.sender?.image!=null){
                Glide.with(context?.applicationContext!!).load(BasicTools.getUrlHttpImg(context!!,
                    notification.sender.image.toString())).into(
                    holder.ivAvatar
                )


        }
        if(BasicTools.isDeviceLanEn()){
            holder.title.setText(notification.titleEn)
            holder.body.setText(notification.bodyEn)
        }
        else{
            holder.title.setText(notification.titleAr)
            holder.body.setText(notification.bodyAr)



        }
    }
    fun addNewItem(item:NotificationItem){
        notifications?.add(item)
        notifyItemInserted(notifications?.size!!-1)

    }

    override fun getItemCount(): Int {
        return notifications!!.size
    }
    class ViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val ivAvatar = view.findViewById<ImageView>(R.id.iv_avatar)
        val notificationImg = view.findViewById<ImageView>(R.id.iv_notification_img)
        val title = view.findViewById<TextView>(R.id.notification_title)
        val body = view.findViewById<TextView>(R.id.notification_body)
        val root=view.findViewById<RelativeLayout>(R.id.root)


    }
}