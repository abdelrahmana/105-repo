package com.urcloset.smartangle.adapter.project105




import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.activity.searchActivity.ISearchActivity
import com.urcloset.smartangle.databinding.ProductItemBinding

import com.urcloset.smartangle.model.project_105.SearchProductV2Model

import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResultProductSearchAdapter(): RecyclerView.Adapter<ResultProductSearchAdapter.AdapterViewHolder>(){

    private var data:ArrayList<SearchProductV2Model.Data.Data> ?= null
    private var context:Context ?=null
    private var iview:ISearchActivity ?=null
    
    var selectedIndex=-1

    private var anim: Animation? = null



    constructor(context:Context,item:ArrayList<SearchProductV2Model.Data.Data>,iview:ISearchActivity) : this() {

        this.context=context
        this.data=item
       this.iview=iview

    }
    var binding : ProductItemBinding? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        binding = ProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AdapterViewHolder(
           /* LayoutInflater.from(parent.context)
                .inflate(R.layout.product_item, parent, false)*/
            binding!!
        )
    }



    fun change_data(new_data:ArrayList<SearchProductV2Model.Data.Data>){
        CoroutineScope(Dispatchers.Main).launch {
            data?.clear()
            for(item in new_data)
                data?.add(item)
            notifyDataSetChanged()


        }

        if(new_data.isNullOrEmpty())
            iview?.checkIfAdapterEmpty(true)
        else iview?.checkIfAdapterEmpty(false)


    }


    fun addItem(item:SearchProductV2Model.Data.Data,pos:Int){
        CoroutineScope(Dispatchers.Main).launch {

            if (pos < data!!.size) run {
                data!!.add(pos, item)
                this@ResultProductSearchAdapter.notifyItemInserted(pos)
            }else run {
                data!!.add(item)
                this@ResultProductSearchAdapter.notifyItemInserted(data!!.size-1)
            }

        }
    }
    fun addRefersh(new_data:ArrayList<SearchProductV2Model.Data.Data>){
        CoroutineScope(Dispatchers.Main).launch {

            for(item in new_data)
                data?.add(item)
            notifyItemInserted(data!!.size-1)
        }

        if(data.isNullOrEmpty())
            iview?.checkIfAdapterEmpty(true)
        else iview?.checkIfAdapterEmpty(false)

    }

    fun getUserDataAt(position: Int): SearchProductV2Model.Data.Data? {
        return if (position >= 0 && position < data!!.size) data!!.get(position)
        else null
    }

    fun getList(): ArrayList<SearchProductV2Model.Data.Data> {
        return data!!
    }

    override fun getItemCount() = data!!.size




    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {

        val item=data?.get(position)

        Log.i("ImageAdapter","IMAGE_URI=$item")


       




        var ivImg=holder.view.ivProduct
       // var tvView=holder.view.tv_views as TextView
        var price=holder.view.price as TextView
        var card=holder.view.rootProduct as LinearLayout


      //  tvView.setText(item?.views.toString())
        price.setText(item?.price.toString())



        if(!item?.itemMedia.isNullOrEmpty()){

            BasicTools.loadImage(BasicTools.getUrlImg(context!!,item?.itemMedia!!.get(0)?.mediaPath!!),
                ivImg,object : DownloadListener {
                    override fun completed(status: Boolean, bitmap: Bitmap) {
                        var anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                        anim!!.setDuration(1000)
                        ivImg.animation= anim



                    }
                })

        }








        card.setOnClickListener {




            val gson = Gson()
            val intent = Intent(context, ProductDetails::class.java)
            intent.putExtra("product", gson.toJson(item))
            context!!.startActivity(intent)




       /*     var selected=true

            data!!.get(position).isSelected = data!!.get(position).isSelected==null||data!!.get(position).isSelected==false
            this.notifyItemChanged(position)*/
        }




      /*  var urlLink= BasicTools.getUrlImg(context!!,item?.photo.toString())
        var image=  holder.view.iv_product_card2 as ImageView
*/
    /*    BasicTools.loadImage(urlLink,image,object : DownloadListener {
            override fun completed(status: Boolean, bitmap: Bitmap) {
                anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                anim!!.setDuration(1000)
                image.animation= anim
            }
        })*/






/*
        holder.view.card_product.setOnClickListener{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               holder.view.iv_card_product.setTransitionName(Constants.TRANSITION+position)
            }
            iview!!.openProductInfo(holder.view.iv_card_product,item!!,Constants.TRANSITION+position)
        }*/



    }



    class AdapterViewHolder(val view: ProductItemBinding) : RecyclerView.ViewHolder(view.root)
}
