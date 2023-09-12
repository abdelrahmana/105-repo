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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.databinding.BookmarkItemBinding
import com.urcloset.smartangle.fragment.bookmark_fragment.IBookMarkFragment
import com.urcloset.smartangle.model.project_105.BookmarkMV3

import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConditionListAdapter(): RecyclerView.Adapter<ConditionListAdapter.AdapterViewHolder>(){

    private var data:ArrayList<BookmarkMV3.Data.Data> ?= null
    private var context:Context ?=null
    private var iview: IBookMarkFragment?=null
    
    var selectedIndex=0

    private var anim: Animation? = null



    constructor(context:Context, item:ArrayList<BookmarkMV3.Data.Data>, iview:IBookMarkFragment) : this() {

        this.context=context
        this.data=item
       this.iview=iview

    }
    var binding : BookmarkItemBinding?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        binding = BookmarkItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AdapterViewHolder(
            binding!!
            /*
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bookmark_item, parent, false)*/
        )
    }



    fun change_data(new_data:ArrayList<BookmarkMV3.Data.Data>){
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


    fun addItem(item:BookmarkMV3.Data.Data, pos:Int){
        CoroutineScope(Dispatchers.Main).launch {

            if (pos < data!!.size) run {
                data!!.add(pos, item)
                this@ConditionListAdapter.notifyItemInserted(pos)
            }else run {
                data!!.add(item)
                this@ConditionListAdapter.notifyItemInserted(data!!.size-1)
            }

        }
    }
    fun addRefersh(new_data:ArrayList<BookmarkMV3.Data.Data>){
        CoroutineScope(Dispatchers.Main).launch {

            for(item in new_data)
                data?.add(item)
            notifyItemInserted(data!!.size-1)
        }


        if(data.isNullOrEmpty())
            iview?.checkIfAdapterEmpty(true)
        else iview?.checkIfAdapterEmpty(false)

    }

    fun getUserDataAt(position: Int): BookmarkMV3.Data.Data? {
        return if (position >= 0 && position < data!!.size) data!!.get(position)
        else null
    }

    fun getList(): ArrayList<BookmarkMV3.Data.Data> {
        return data!!
    }


    override fun getItemCount() = data!!.size


     fun getSelectedItem():BookmarkMV3.Data.Data{


         return data?.get(selectedIndex)!!
     }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {

        val item=data?.get(position)

        Log.i("ImageAdapter","IMAGE_URI=$item")


       




        var name=holder.view.tvTitle as TextView
        var desc=holder.view.tvDesc as TextView
        var ivBookmark=holder.view.ivBookmark as ImageView
        var ivImg=holder.view.ivImg as ImageView
     //    var root =holder.view.root as LinearLayout

        
        



        if(BasicTools.isDeviceLanEn()){
           name.setText(item?.name)
           desc.setText(item?.description)
        }
        else{
            name.setText(item?.name)
        desc.setText(item?.description)

        }



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











        ivBookmark.setOnClickListener{

            iview!!.delete(item?.id!!,position)

        }
        binding?.rootLinear?.setOnClickListener {
            val intent = Intent(context, ProductDetails::class.java)
            val gson = Gson()
            intent.putExtra("product", gson.toJson(data?.get(position)))
            context?.startActivity(intent)

        }



    }



    class AdapterViewHolder(val view: BookmarkItemBinding) : RecyclerView.ViewHolder(view.root)
}
