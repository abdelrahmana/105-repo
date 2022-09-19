package com.urcloset.smartangle.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.model.SizeModel

class SizeAdapterProductDetails(var lang: String): ListAdapter<SizeModel.Size, SizeAdapterProductDetails.ViewHolder>(object :
    DiffUtil.ItemCallback<SizeModel.Size>() {
    override fun areItemsTheSame(oldItem: SizeModel.Size, newItem: SizeModel.Size): Boolean {
        return newItem.id == newItem.id


    }

    override fun areContentsTheSame(
        oldItem: SizeModel.Size,
        newItem: SizeModel.Size
    ): Boolean {
        return false
    }
}){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.size_item, parent, false)
        return SizeAdapterProductDetails.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val size= getItem(position)
        if(lang == "en")
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

    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.name)

    }


}