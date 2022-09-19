package com.urcloset.smartangle.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.urcloset.smartangle.R
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.model.ProductStateModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener

class ProductsNewAdapter(): RecyclerView.Adapter<ProductsNewAdapter.AdapterViewHolder>() {
    private var products:ArrayList<ProductModel.Product> ?= null
    private var context:Context ?=null
    var imageIndex =0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductsNewAdapter.AdapterViewHolder {
        val view : View= LayoutInflater.from(parent.context)
            .inflate(R.layout.product_new_layout, parent, false)
        return ProductsNewAdapter.AdapterViewHolder(view)
    }
    constructor(context: Context, products:ArrayList<ProductModel.Product>) : this() {

        this.context=context
        this.products=products

    }



    override fun onBindViewHolder(holder: ProductsNewAdapter.AdapterViewHolder, position: Int) {
         imageIndex =0

        val product = products?.get(position)
        holder.tvPrice.text = product?.price.toString()+" $"
        holder.tvViews.text = product?.views.toString()
        if(imageIndex<=product?.itemMedia?.size!!-1)
            if (product.itemMedia.get(imageIndex) != null && !product.itemMedia.get(imageIndex)!!.mediaPath.isNullOrEmpty())
        Glide.with(holder.itemView.context.applicationContext)
            .load( BasicTools.getUrlHttpImg(holder.itemView.context,
                product.itemMedia.get(imageIndex)!!.mediaPath!!))
            .into(holder.image)



        holder.ivNext.setOnClickListener {
            if(imageIndex<product.itemMedia.size-1) {
                imageIndex++
                if (product.itemMedia.get(imageIndex) != null && !product.itemMedia.get(imageIndex)!!.mediaPath.isNullOrEmpty())
                Glide.with(holder.itemView.context.applicationContext)
                    .load( BasicTools.getUrlHttpImg(holder.itemView.context,
                        product.itemMedia.get(imageIndex)!!.mediaPath!!))
                    .into(holder.image)
            }

        }
        holder.ivPrev.setOnClickListener {
            if (imageIndex > 0) {
                imageIndex--
                if (product.itemMedia.get(imageIndex) != null && !product.itemMedia.get(imageIndex)!!.mediaPath.isNullOrEmpty())
                    Glide.with(holder.itemView.context.applicationContext)
                        .load(
                            BasicTools.getUrlHttpImg(
                                holder.itemView.context,
                                product.itemMedia.get(imageIndex)!!.mediaPath!!
                            )
                        )
                        .into(holder.image)

            }
        }
        holder.image.setOnClickListener {
                itemClickListener.onClick(position)

        }


    }
    fun setOnProductClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener =itemClickListener

    }
    fun loadImage(context:Context,path:String,image:ImageView){
        BasicTools.loadImage(
            BasicTools.getUrlImg(context,
               path),
            image,object : DownloadListener {
                override fun completed(status: Boolean, bitmap: Bitmap) {
                    //anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                    //anim!!.setDuration(1000)
                }
            })
    }

    override fun getItemCount(): Int {
        return products!!.size;
    }
    lateinit var itemClickListener :ItemClickListener
    class AdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image:ImageView = view.findViewById(R.id.main_product_image)
        val tvViews:TextView = view.findViewById(R.id.tv_views)
        val tvPrice:TextView = view.findViewById(R.id.tv_price)
        val ivNext:ImageView = view.findViewById(R.id.iv_next)
        val ivPrev:ImageView = view.findViewById(R.id.iv_prev)


    }
    }