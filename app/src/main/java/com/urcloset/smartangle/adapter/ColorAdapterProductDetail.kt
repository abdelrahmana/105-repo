package com.urcloset.smartangle.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.databinding.ColorItemBinding
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.ColorModel

class ColorAdapterProductDetail() : ListAdapter<ColorModel.Color, ColorAdapterProductDetail.ViewHolder>
    (object :
    DiffUtil.ItemCallback<ColorModel.Color>() {

    override fun areItemsTheSame(oldItem: ColorModel.Color, newItem: ColorModel.Color): Boolean {
        return oldItem.id == newItem.id

    }

    override fun areContentsTheSame(
        oldItem: ColorModel.Color,
        newItem: ColorModel.Color
    ): Boolean {
        return oldItem.isSelected == newItem.isSelected
    }
}) {
    var binding : ColorItemBinding?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapterProductDetail.ViewHolder {
      binding = ColorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
   //     val view = LayoutInflater.from(parent.context).inflate(R.layout.color_item, parent, false)
        return ViewHolder(binding!!)
    }



    override fun onBindViewHolder(holder: ColorAdapterProductDetail.ViewHolder, position: Int) {
        holder.colorImage.backgroundTintList=(ColorStateList.valueOf(Color.parseColor(getItem(position).value)))

     /*   holder.colorImage.background.setColorFilter(
            Color.parseColor(getItem(position).value),
            PorterDuff.Mode.MULTIPLY
        )*/
//        if (getItem(position).isSelected!!>0) {
//            holder.ivColorSelected.visibility = View.VISIBLE
//        } else {
//
//            holder.ivColorSelected.visibility = View.GONE
//        }

            holder.ivColorSelected.visibility = View.GONE


    }
    private var onColorListener: ItemClickListener? = null
    fun setOnColorListener(onColorListener: ItemClickListener?) {
        this.onColorListener = onColorListener
    }

    class ViewHolder(itemView: ColorItemBinding) : RecyclerView.ViewHolder(itemView.root)
    {
        val colorImage: RelativeLayout = itemView.rvColor//itemView.findViewById(R.id.rv_color)
        val ivColorSelected: ImageView =itemView.ivColorSelected// itemView.findViewById(R.id.iv_color_selected)



    }
}
