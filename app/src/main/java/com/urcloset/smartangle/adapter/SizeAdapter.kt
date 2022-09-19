package com.urcloset.smartangle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.model.SizeModel.Size
import com.urcloset.smartangle.tools.BasicTools

class SizeAdapter(var lang: String): ListAdapter<Size, SizeAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<Size>() {
    override fun areItemsTheSame(oldItem: Size, newItem: Size): Boolean {
        return newItem.id == newItem.id


    }

    override fun areContentsTheSame(
        oldItem: Size,
        newItem: Size
    ): Boolean {
        return false
    }
}){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.size_item, parent, false)
        return SizeAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val size= getItem(position)
        if(BasicTools.isDeviceLanEn())
            holder.name.text = size.nameEn
        else holder.name.text = size.nameAr
        if(size.isSelected!!>0){
            holder.name.background = holder.itemView.resources.getDrawable(R.drawable.selected_size_text_button_bg)
            holder.name.setTextColor(holder.itemView.context.resources.getColor(R.color.white))

        }
        else{
            holder.name.background = holder.itemView.resources.getDrawable(R.drawable.unselected_size_text_button_bg)
            holder.name.setTextColor(holder.itemView.context.resources.getColor(R.color.black))


        }
        holder.name.setOnClickListener {
            if(size.isSelected!!>0){
                holder.name.background = holder.itemView.resources.getDrawable(R.drawable.unselected_size_text_button_bg)
                currentList.get(position).isSelected= 0
                holder.name.setTextColor(holder.itemView.context.resources.getColor(R.color.black))


            }
            else {
                holder.name.background = holder.itemView.resources.getDrawable(R.drawable.selected_size_text_button_bg)
                currentList.get(position).isSelected = 1
                holder.name.setTextColor(holder.itemView.context.resources.getColor(R.color.white))


            }

        }
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)

    }


}