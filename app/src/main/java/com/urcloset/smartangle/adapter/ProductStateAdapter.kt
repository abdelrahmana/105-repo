package com.urcloset.smartangle.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.activity.updateProduct.UpdateProductActivity
import com.urcloset.smartangle.model.ProductDetailsModel
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.model.ProductStateModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener

class ProductStateAdapter(): RecyclerView.Adapter<ProductStateAdapter.AdapterViewHolder>() {
    private var products:ArrayList<ProductModel.Product> ?= null
    private var context:Context ?=null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductStateAdapter.AdapterViewHolder {
         val view : View= LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return AdapterViewHolder(view)

    }
    constructor(context: Context, item:ArrayList<ProductModel.Product>) : this() {

        this.context=context
        this.products=item

    }

    override fun onBindViewHolder(holder: ProductStateAdapter.AdapterViewHolder, position: Int) {
        val product:ProductModel.Product = products?.get(position)!!
        if(product.itemMedia?.size!!>0)
        Glide.with(holder.itemView.context.applicationContext).load(BasicTools.getUrlHttpImg(holder.itemView.context,
            product.itemMedia.get(0)!!.mediaPath!!)).into(holder.ivImage)
        holder.tvPrice.setText(product.price.toString()+" $")
        holder.root.setOnClickListener {
            val intent = Intent(context, ProductDetails::class.java)
            val gson = Gson()
            intent.putExtra("product", gson.toJson(product))
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return products!!.size
    }
    class AdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val ivImage= view.findViewById<ImageView>(R.id.iv_product)
        val tvPrice = view.findViewById<TextView>(R.id.price)
        val ivOptions= view.findViewById<ImageView>(R.id.iv_info)
        val root = view.findViewById<LinearLayout>(R.id.root_product)


    }
}