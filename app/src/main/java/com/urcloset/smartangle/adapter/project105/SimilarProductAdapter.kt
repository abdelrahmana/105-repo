package com.urcloset.smartangle.adapter.project105




import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.media.Image

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.IProductDetailsActivity
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.databinding.NewProductItemBinding
import com.urcloset.smartangle.model.ProductDetailsModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SimilarProductAdapter(): RecyclerView.Adapter<SimilarProductAdapter.AdapterViewHolder>(){

    private var data:ArrayList<ProductDetailsModel.Data.SimilarProduct> ?= null
    private var context:Context ?=null
    private var iview:IProductDetailsActivity ?=null

    private var anim: Animation? = null



    constructor(context:Context, item:ArrayList<ProductDetailsModel.Data.SimilarProduct>, iview:IProductDetailsActivity) : this() {

        this.context=context
        this.data=item
       this.iview=iview

    }
    var binding : NewProductItemBinding?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        binding = NewProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AdapterViewHolder(
           /* LayoutInflater.from(parent.context)
                .inflate(R.layout.new_product_item, parent, false)*/
        binding!!
        )
    }



    fun change_data(new_data:ArrayList<ProductDetailsModel.Data.SimilarProduct>){
        CoroutineScope(Dispatchers.Main).launch {
            data?.clear()
            for(item in new_data)
                data?.add(item)
            notifyDataSetChanged()

        }


    }


    fun addItem(item:ProductDetailsModel.Data.SimilarProduct, pos:Int){
        CoroutineScope(Dispatchers.Main).launch {

            if (pos < data!!.size) run {
                data!!.add(pos, item)
                this@SimilarProductAdapter.notifyItemInserted(pos)
            }else run {
                data!!.add(item)
                this@SimilarProductAdapter.notifyItemInserted(data!!.size-1)
            }

        }
    }
    fun addRefersh(new_data:ArrayList<ProductDetailsModel.Data.SimilarProduct>){
        CoroutineScope(Dispatchers.Main).launch {

            for(item in new_data)
                data?.add(item)
            notifyItemInserted(data!!.size-1)
        }

    }

    fun getUserDataAt(position: Int): ProductDetailsModel.Data.SimilarProduct? {
        return if (position >= 0 && position < data!!.size) data!!.get(position)
        else null
    }

    fun getList(): ArrayList<ProductDetailsModel.Data.SimilarProduct> {
        return data!!
    }

    override fun getItemCount() = data!!.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {

        val item=data?.get(position)

        Log.i("ImageAdapter1234","IMAGE_URI=$item")


       

/*        if(BasicTools.isDeviceLanEn())
        holder.view.tv_product_name_card2.setText(item?.enName)
        else
            holder.view.tv_product_name_card2.setText(item?.arName)
        var txt=context!!.resources.getString(R.string.quantity)*/


        var ivImage=holder.view.ivProduct as ImageView
        var tvViews=holder.view.tvViews as TextView
        var ivOptions=holder.view.ivInfo as ImageView
      //  var root=holder.view.root as LinearLayout

        if(item!!.itemMedia!=null && item.itemMedia?.size!!>0) {
            val urlLink= BasicTools.getUrlImg(context!!,item?.itemMedia?.get(0)?.mediaPath.toString())
            BasicTools.loadImage(
                urlLink,
                ivImage, object : DownloadListener {
                    override fun completed(status: Boolean, bitmap: Bitmap) {
                        var anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                        anim!!.setDuration(1000)
                        ivImage.animation = anim
                    }
                })
        }
        tvViews.text = item.views.toString()

       binding?.rootItem?.setOnClickListener {
            val intent = Intent(context, ProductDetails::class.java)
            val gson = Gson()
            intent.putExtra("product", gson.toJson(item))
            context?.startActivity(intent)
        }













    }



    class AdapterViewHolder(val view: NewProductItemBinding) : RecyclerView.ViewHolder(view.root)
}
