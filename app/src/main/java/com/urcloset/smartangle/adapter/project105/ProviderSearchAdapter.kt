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
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.searchActivity.ISearchProviderActivity
import com.urcloset.smartangle.activity.sellerActivity.SellerActivity
import com.urcloset.smartangle.databinding.UserItemBinding

import com.urcloset.smartangle.model.project_105.BookmarkMV3
import com.urcloset.smartangle.model.project_105.ProivderSeachModel

import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProviderSearchAdapter(): RecyclerView.Adapter<ProviderSearchAdapter.AdapterViewHolder>(){

    private var data:ArrayList<ProivderSeachModel.Data.Data> ?= null
    private var context:Context ?=null

    

    private var iview:ISearchProviderActivity?=null

    private var anim: Animation? = null



    constructor(context:Context, item:ArrayList<ProivderSeachModel.Data.Data>,iview:ISearchProviderActivity) : this() {

        this.context=context
        this.data=item
        this.iview=iview


    }
    var binding : UserItemBinding? =null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        binding = UserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AdapterViewHolder(
           /* LayoutInflater.from(parent.context)
                .inflate(R.layout.user_item, parent, false)*/
            binding!!
        )
    }



    fun change_data(new_data:ArrayList<ProivderSeachModel.Data.Data>){
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


    fun addItem(item:ProivderSeachModel.Data.Data, pos:Int){
        CoroutineScope(Dispatchers.Main).launch {

            if (pos < data!!.size) run {
                data!!.add(pos, item)
                this@ProviderSearchAdapter.notifyItemInserted(pos)
            }else run {
                data!!.add(item)
                this@ProviderSearchAdapter.notifyItemInserted(data!!.size-1)
            }

        }
    }
    fun addRefersh(new_data:ArrayList<ProivderSeachModel.Data.Data>){
        CoroutineScope(Dispatchers.Main).launch {

            for(item in new_data)
                data?.add(item)
            notifyItemInserted(data!!.size-1)
        }

        if(data.isNullOrEmpty())
            iview?.checkIfAdapterEmpty(true)
        else iview?.checkIfAdapterEmpty(false)




    }

    fun getUserDataAt(position: Int): ProivderSeachModel.Data.Data? {
        return if (position >= 0 && position < data!!.size) data!!.get(position)
        else null
    }

    fun getList(): ArrayList<ProivderSeachModel.Data.Data> {
        return data!!
    }


    override fun getItemCount() = data!!.size



    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {

        val item=data?.get(position)

        Log.i("ImageAdapter","IMAGE_URI=$item")


       




        var name=holder.view.tvTitle as TextView
   
        var root=holder.view.visit as CardView
        var ivImg=holder.view.ivUserImg as ImageView

        


        name.setText(item?.name)







        if(!item?.image.isNullOrEmpty()){

            BasicTools.loadUsersImage(BasicTools.getUrlImg(context!!,item?.image!!),
                ivImg,object : DownloadListener {
                    override fun completed(status: Boolean, bitmap: Bitmap) {
                        var anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                        anim!!.setDuration(1000)
                        ivImg.animation= anim



                    }
                })

        }











        root.setOnClickListener{
            val intent = Intent(holder.itemView.context, SellerActivity::class.java)
            intent.putExtra("identifier",item?.id.toString())
            holder.itemView.context.startActivity(intent)

        }



    }



    class AdapterViewHolder(val view: UserItemBinding) : RecyclerView.ViewHolder(view.root)
}
