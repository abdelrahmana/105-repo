package com.urcloset.smartangle.adapter

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.urcloset.smartangle.R
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.CategoryModel.Category
import com.urcloset.smartangle.model.PersonalUserInfoModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.DownloadListener

class CategoryAdapter(
) : ListAdapter<Category, CategoryAdapter.ViewHolder>(  object : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id ==newItem.id

    }

    override fun areContentsTheSame(
        oldItem: Category,
        newItem: Category
    ): Boolean {
        return oldItem.nameEn == newItem.nameEn
    }
})

{
    var selected :Int = 0
    lateinit var itemClickListener : ItemClickListener
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)

        return ViewHolder(view)
    }
    fun setOnCategoryClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener = itemClickListener


    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category = getItem(position)
        if(!category.mediaPath.isNullOrEmpty())
                Glide.with(holder.itemView.context.applicationContext)
                    .load( BasicTools.getUrlHttpImg(holder.itemView.context,
                        category.mediaPath))
                    .into(holder.imageView)

        if(position == selected) {
            holder.catBg.background =
                holder.itemView.resources.getDrawable(R.drawable.category_selected_item_bg)
//            holder.redCircle.visibility= View.VISIBLE

        }
        else
            holder.catBg.background = holder.itemView.resources.getDrawable(R.drawable.category_unselected_item_bg)
           holder.imageView.setOnClickListener(View.OnClickListener {
               selected = position
               notifyDataSetChanged()
               itemClickListener.onClick(position)
        })


        // sets the text to the textview from our itemHolder class

    }

    // return the number of the items in the list


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_photo)
        val redCircle: ImageView = itemView.findViewById(R.id.iv_new_products)
        val catBg: RelativeLayout = itemView.findViewById(R.id.rv_cat)


    }
}
