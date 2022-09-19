package com.urcloset.smartangle.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.listeners.OnProductChange
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.model.project_105.ConditionBookMarkModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyProductStateAdapter(): RecyclerView.Adapter<MyProductStateAdapter.AdapterViewHolder>() {
    private var products:ArrayList<ProductModel.Product> ?= null
    private var context: Context?=null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyProductStateAdapter.AdapterViewHolder {
        val view : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.new_product_item, parent, false)
        return AdapterViewHolder(view)

    }
    constructor(context: Context, item:ArrayList<ProductModel.Product>) : this() {

        this.context=context
        this.products=item

    }
    var state :Int = 0

    override fun onBindViewHolder(holder: MyProductStateAdapter.AdapterViewHolder, position: Int) {
        val product: ProductModel.Product = products?.get(position)!!
        if(product.itemMedia?.size!!>0)
        BasicTools.loadImage(
            BasicTools.getUrlHttpImg(holder.itemView.context,
            product.itemMedia.get(0)!!.mediaPath!!),
            holder.ivImage,object : DownloadListener {
                override fun completed(status: Boolean, bitmap: Bitmap) {
                    //anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                    //anim!!.setDuration(1000)
                }
            })
        holder.tvViews.text = product.views.toString()
        holder.ivOptions.setOnClickListener {

            if(state==1) {
                val popmenu = PopupMenu(context, it)
                popmenu.inflate(R.menu.menu_new_blocked)
                popmenu.show()
                popmenu.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.product_new -> {
                            onProductChangeListener.onProductChange(position,1, products!![position])
                        }
                        R.id.blocked -> {
                            onProductChangeListener.onProductChange(position,3,products!![position])

                        }

                    }
                    true
                }
            }
            else if(state==2){
                val popmenu = PopupMenu(context, it)
                popmenu.inflate(R.menu.menu_new_selled)
                popmenu.show()
                popmenu.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.product_new -> {
                            onProductChangeListener.onProductChange(position,1,products!![position])
                        }
                        R.id.selled -> {
                            onProductChangeListener.onProductChange(position,2,products!![position])

                        }

                    }
                    true
                }


            }
        }
        holder.root.setOnClickListener {
            val intent = Intent(context, ProductDetails::class.java)
            val gson = Gson()
            intent.putExtra("product", gson.toJson(product))
            context?.startActivity(intent)
        }
    }

    fun change_data(new_data:ArrayList<ProductModel.Product>){
        CoroutineScope(Dispatchers.Main).launch {
            products?.clear()
            for(item in new_data)
                products?.add(item)
            notifyDataSetChanged()

        }


    }
    fun setData(new_data:ArrayList<ProductModel.Product>){
            products = new_data
            notifyDataSetChanged()

        }



    fun addData(new_data:ProductModel.Product){
        CoroutineScope(Dispatchers.Main).launch {
                products?.add(new_data)
            notifyDataSetChanged()

        }


    }
    fun removeItem(item:Int){
        CoroutineScope(Dispatchers.Main).launch {
            products?.removeAt(item)
            notifyDataSetChanged()

        }


    }
   fun setOnProductChange(onProductChange:OnProductChange){
       this.onProductChangeListener = onProductChange

    }
   lateinit var onProductChangeListener:OnProductChange

    override fun getItemCount(): Int {
        return products!!.size
    }
    class AdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        val ivImage= view.findViewById<ImageView>(R.id.iv_product)
        val tvViews= view.findViewById<TextView>(R.id.tv_views)
        val ivOptions= view.findViewById<ImageView>(R.id.iv_info)
        val root = view.findViewById<LinearLayout>(R.id.root)


    }
}