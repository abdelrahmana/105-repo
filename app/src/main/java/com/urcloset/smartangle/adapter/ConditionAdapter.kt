package com.urcloset.smartangle.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.ConditionModel.Condition
import com.urcloset.smartangle.tools.BasicTools

class ConditionAdapter(var lang: String): ListAdapter<Condition, ConditionAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<Condition>() {
    override fun areItemsTheSame(oldItem: Condition, newItem: Condition): Boolean {
        return newItem.id == newItem.id


    }

    override fun areContentsTheSame(
        oldItem: Condition,
        newItem: Condition
    ): Boolean {
        return false
    }
})


{
    var selected = 0


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.condition_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val condition = getItem(position)
        if(BasicTools.isDeviceLanEn()){
            holder.name.text = condition.nameEn
        }
        else {
            holder.name.text = condition.nameAr


        }
        if(position == selected){
            holder.name.setTextColor(Color.parseColor("#ffffff"))
            holder.name.background = holder .itemView.context.resources.getDrawable(R.drawable.yes_condition_background)

        }
        else{
            holder.name.background =
                holder.itemView.context.resources.getDrawable(R.drawable.ng_condition_background)
            holder.name.setTextColor(Color.parseColor("#000000"))

        }
        holder.name.setOnClickListener {
            selected = position
            this.ItemClickListener!!.onClick(position)
            holder.name.setTextColor(Color.parseColor("#ffffff"))
            holder.name.background = holder .itemView.context.resources.getDrawable(R.drawable.yes_condition_background)
            for (i in 0..currentList.size-1){
                if(i!=position) {
                    holder.name.background =
                        holder.itemView.context.resources.getDrawable(R.drawable.ng_condition_background)
                    holder.name.setTextColor(Color.parseColor("#000000"))
                }

            }
            notifyDataSetChanged()

        }
    }
     var ItemClickListener :ItemClickListener? =null
    fun setOnConditionItemSelected(itemClickListener:ItemClickListener){
        this.ItemClickListener = itemClickListener


    }


    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)



    }
}