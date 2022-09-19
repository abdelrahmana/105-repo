package com.urcloset.smartangle.adapter.project105




import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.conditionActivity.IConditionActivity
import com.urcloset.smartangle.model.ConditionModel
import com.urcloset.smartangle.model.project_105.ColorModelHassan
import com.urcloset.smartangle.model.project_105.ConditionBookMarkModel

import com.urcloset.smartangle.tools.BasicTools
import kotlinx.android.synthetic.main.color_item.view.*


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChooseColorAdapter(): RecyclerView.Adapter<ChooseColorAdapter.AdapterViewHolder>(){

    private var data:ArrayList<ColorModelHassan.Color> ?= null
    private var context:Context ?=null
 //   private var iview:IConditionActivity ?=null
    
    var selectedIndex=0

    private var anim: Animation? = null



    constructor(context:Context,item:ArrayList<ColorModelHassan.Color>) : this() {

        this.context=context
        this.data=item
      // this.iview=iview

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        return AdapterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.color_item, parent, false)
        )
    }



    fun change_data(new_data:ArrayList<ColorModelHassan.Color>){
        CoroutineScope(Dispatchers.Main).launch {
            data?.clear()
            for(item in new_data)
                data?.add(item)
            notifyDataSetChanged()

        }


    }


    fun addItem(item:ColorModelHassan.Color,pos:Int){
        CoroutineScope(Dispatchers.Main).launch {

            if (pos < data!!.size) run {
                data!!.add(pos, item)
                this@ChooseColorAdapter.notifyItemInserted(pos)
            }else run {
                data!!.add(item)
                this@ChooseColorAdapter.notifyItemInserted(data!!.size-1)
            }

        }
    }
    fun addRefersh(new_data:ArrayList<ColorModelHassan.Color>){
        CoroutineScope(Dispatchers.Main).launch {

            for(item in new_data)
                data?.add(item)
            notifyItemInserted(data!!.size-1)
        }

    }

    fun getUserDataAt(position: Int): ColorModelHassan.Color? {
        return if (position >= 0 && position < data!!.size) data!!.get(position)
        else null
    }

    fun getList(): ArrayList<ColorModelHassan.Color> {
        return data!!
    }

    override fun getItemCount() = data!!.size


     fun getSelectedItem():ColorModelHassan.Color{


         return data?.get(selectedIndex)!!
     }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {

        val item=data?.get(position)

        Log.i("ImageAdapter","IMAGE_URI=$item")


       




        var root=holder.view.rv_color as RelativeLayout
        var selectedIV=holder.view.iv_color_selected as ImageView


        
        root.background.setColorFilter(
            Color.parseColor(item!!.value),
            PorterDuff.Mode.MULTIPLY
        )
        
        if(item.isSelected!=null && item.isSelected!!){
            selectedIV.visibility=View.VISIBLE

            
        }else{
            selectedIV.visibility=View.GONE
        }






        root.setOnClickListener {



            var selected=false

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
