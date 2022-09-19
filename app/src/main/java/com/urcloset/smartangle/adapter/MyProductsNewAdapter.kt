package com.urcloset.smartangle.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.urcloset.smartangle.R
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.listeners.OnProductChange
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener

class MyProductsNewAdapter(): RecyclerView.Adapter<MyProductsNewAdapter.AdapterViewHolder>() {
    private var products:ArrayList<ProductModel.Product> ?= null
    private var context:Context ?=null
    var imageIndex =0
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyProductsNewAdapter.AdapterViewHolder {
        val view : View= LayoutInflater.from(parent.context)
            .inflate(R.layout.my_product_new_layout, parent, false)
        return MyProductsNewAdapter.AdapterViewHolder(view)
    }
    constructor(context: Context, products:ArrayList<ProductModel.Product>) : this() {

        this.context=context
        this.products=products

    }

    override fun onBindViewHolder(holder: MyProductsNewAdapter.AdapterViewHolder, position: Int) {
        imageIndex =0

        val product = products?.get(position)
        holder.tvPrice.text = product?.price.toString()+" $"
        holder.tvViews.text = product?.views.toString()
        if(imageIndex<=product?.itemMedia?.size!!-1)
            Glide.with(holder.itemView.context.applicationContext).load(BasicTools.getUrlHttpImg(holder.itemView.context,
                product.itemMedia.get(0)!!.mediaPath!!)).into(holder.image)
        holder.ivNext.setOnClickListener {
            if(imageIndex<product.itemMedia.size-1) {
                imageIndex++
                Glide.with(holder.itemView.context).load(BasicTools.getUrlHttpImg(holder.itemView.context.applicationContext,
                    product.itemMedia.get(0)!!.mediaPath!!)).into(holder.image)
            }

        }
        holder.ivPrev.setOnClickListener {
            if(imageIndex>0) {
                imageIndex--
                Glide.with(holder.itemView.context).load(BasicTools.getUrlHttpImg(holder.itemView.context.applicationContext,
                    product.itemMedia.get(0)!!.mediaPath!!)).into(holder.image)           }

        }
        holder.image.setOnClickListener {
            itemClickListener.onClick(position)

        }
        holder.option.setOnClickListener {
            val popupMenu = PopupMenu(context,it)
            popupMenu.inflate(R.menu.menu_blocked_selled)
            popupMenu.show()
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.selled -> {
                        onProductChangeListener.onProductChange(position,2,products!![position])
                    }
                    R.id.blocked -> {
                        onProductChangeListener.onProductChange(position,3,products!![position],)

                    }

                }
                true
            }

        }



    }

    fun setOnProductClickListener(itemClickListener: ItemClickListener){
        this.itemClickListener =itemClickListener

    }
    fun setOnProductChange(onProductChange: OnProductChange){
        this.onProductChangeListener =onProductChange

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
    lateinit var onProductChangeListener: OnProductChange

    class AdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val image:ImageView = view.findViewById(R.id.main_product_image)
        val tvViews:TextView = view.findViewById(R.id.tv_views)
        val tvPrice:TextView = view.findViewById(R.id.tv_price)
        val ivNext:ImageView = view.findViewById(R.id.iv_next)
        val ivPrev:ImageView = view.findViewById(R.id.iv_prev)
        val option :ImageView = view.findViewById(R.id.iv_new_option)


    }
}