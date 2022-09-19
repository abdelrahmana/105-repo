package com.urcloset.smartangle.adapter.project105




import android.annotation.SuppressLint
import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.urcloset.smartangle.R
import com.urcloset.smartangle.fragment.bookmark_fragment.IBookMarkFragment
import com.urcloset.smartangle.model.project_105.BookmarkMV3

import kotlinx.android.synthetic.main.bookmark_item.view.*


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookMarkAdapter(): RecyclerView.Adapter<BookMarkAdapter.AdapterViewHolder>(){

    private var data:ArrayList<BookmarkMV3.Data.Data> ?= null
    private var context:Context ?=null
    private var iview:IBookMarkFragment ?=null

    private var anim: Animation? = null



    constructor(context:Context, item:ArrayList<BookmarkMV3.Data.Data>, iview:IBookMarkFragment) : this() {

        this.context=context
        this.data=item
       this.iview=iview

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bookmark_item, parent, false)
        )
    }



    fun change_data(new_data:ArrayList<BookmarkMV3.Data.Data>){
        CoroutineScope(Dispatchers.Main).launch {
            data?.clear()
            for(item in new_data)
                data?.add(item)
            notifyDataSetChanged()

        }


    }


    fun addItem(item:BookmarkMV3.Data.Data, pos:Int){
        CoroutineScope(Dispatchers.Main).launch {

            if (pos < data!!.size) run {
                data!!.add(pos, item)
                this@BookMarkAdapter.notifyItemInserted(pos)
            }else run {
                data!!.add(item)
                this@BookMarkAdapter.notifyItemInserted(data!!.size-1)
            }

        }
    }
    fun addRefersh(new_data:ArrayList<BookmarkMV3.Data.Data>){
        CoroutineScope(Dispatchers.Main).launch {

            for(item in new_data)
                data?.add(item)
            notifyItemInserted(data!!.size-1)
        }

    }

    fun getUserDataAt(position: Int): BookmarkMV3.Data.Data? {
        return if (position >= 0 && position < data!!.size) data!!.get(position)
        else null
    }

    fun getList(): ArrayList<BookmarkMV3.Data.Data> {
        return data!!
    }

    override fun getItemCount() = data!!.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {

        val item=data?.get(position)

        Log.i("ImageAdapter","IMAGE_URI=$item")


       

/*        if(BasicTools.isDeviceLanEn())
        holder.view.tv_product_name_card2.setText(item?.enName)
        else
            holder.view.tv_product_name_card2.setText(item?.arName)
        var txt=context!!.resources.getString(R.string.quantity)*/


        var name=holder.view.tv_title as TextView
        var desc=holder.view.tv_desc as TextView

        name.setText(item?.name)
        desc.setText(item?.description)


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



    class AdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
