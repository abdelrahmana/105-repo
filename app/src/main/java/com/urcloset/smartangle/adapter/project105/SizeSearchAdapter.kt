package com.urcloset.smartangle.adapter.project105




import android.annotation.SuppressLint
import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.conditionActivity.IConditionActivity
import com.urcloset.smartangle.model.ConditionModel
import com.urcloset.smartangle.model.project_105.ConditionBookMarkModel
import com.urcloset.smartangle.model.project_105.SizeModelHassan

import com.urcloset.smartangle.tools.BasicTools

import kotlinx.android.synthetic.main.card_condition_bookmark_v2.view.*



import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SizeSearchAdapter(): RecyclerView.Adapter<SizeSearchAdapter.AdapterViewHolder>(){

    private var data:ArrayList<SizeModelHassan.Size> ?= null
    private var context:Context ?=null
   // private var iview:IConditionActivity ?=null
    
    var selectedIndex=0

    private var anim: Animation? = null



    constructor(context:Context,item:ArrayList<SizeModelHassan.Size>) : this() {

        this.context=context
        this.data=item
      // this.iview=iview

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_condition_bookmark_v2, parent, false)
        )
    }



    fun change_data(new_data:ArrayList<SizeModelHassan.Size>){
        CoroutineScope(Dispatchers.Main).launch {
            data?.clear()
            for(item in new_data)
                data?.add(item)
            notifyDataSetChanged()

        }


    }


    fun addItem(item:SizeModelHassan.Size,pos:Int){
        CoroutineScope(Dispatchers.Main).launch {

            if (pos < data!!.size) run {
                data!!.add(pos, item)
                this@SizeSearchAdapter.notifyItemInserted(pos)
            }else run {
                data!!.add(item)
                this@SizeSearchAdapter.notifyItemInserted(data!!.size-1)
            }

        }
    }
    fun addRefersh(new_data:ArrayList<SizeModelHassan.Size>){
        CoroutineScope(Dispatchers.Main).launch {

            for(item in new_data)
                data?.add(item)
            notifyItemInserted(data!!.size-1)
        }

    }

    fun getUserDataAt(position: Int): SizeModelHassan.Size? {
        return if (position >= 0 && position < data!!.size) data!!.get(position)
        else null
    }

    fun getList(): ArrayList<SizeModelHassan.Size> {
        return data!!
    }

    override fun getItemCount() = data!!.size


     fun getSelectedItem():SizeModelHassan.Size{


         return data?.get(selectedIndex)!!
     }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {

        val item=data?.get(position)

        Log.i("ImageAdapter","IMAGE_URI=$item")


       




        var name=holder.view.tv_condition_name as TextView
        var card=holder.view.card_condition as CardView

        
        
        if(item!!.isSelected==null || !item?.isSelected!!){
            card.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.white))
            name.setTextColor(ContextCompat.getColor(context!!,R.color.login_btn))
            card.radius=BasicTools.dpToPxFloat(10,context!!)

            
        }else{

            card.setCardBackgroundColor(ContextCompat.getColor(context!!,R.color.login_btn))
            name.setTextColor(ContextCompat.getColor(context!!,R.color.white))
            card.radius=BasicTools.dpToPxFloat(10,context!!)

        }


        if(BasicTools.isDeviceLanEn())
           name.setText(item?.nameEn)
        else
            name.setText(item?.nameAr)





        card.setOnClickListener {

            var selected=true

            data!!.get(position).isSelected = data!!.get(position).isSelected==null||data!!.get(position).isSelected==false
            this.notifyItemChanged(position)
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



    class AdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
