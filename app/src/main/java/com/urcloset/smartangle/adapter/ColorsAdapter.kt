package com.urcloset.smartangle.adapter
import android.graphics.Color
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.ColorModel

class ColorsAdapter() :ListAdapter<ColorModel.Color, ColorsAdapter.ViewHolder>(object :
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.color_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorsAdapter.ViewHolder, position: Int) {
        holder.colorImage.background.setColorFilter(
            Color.parseColor(getItem(position).value),
            PorterDuff.Mode.MULTIPLY
        )

        if (getItem(position).isSelected!!>0) {
            holder.ivColorSelected.visibility = View.VISIBLE
        } else {

            holder.ivColorSelected.visibility = View.GONE
        }
        holder.colorImage.setOnClickListener {
            val color: ColorModel.Color =
                currentList[position]

            if (color.isSelected!!>0) {
                color.isSelected= 0
                holder.ivColorSelected.visibility = View.GONE
            } else {
                color.isSelected= 1
                holder.ivColorSelected.visibility = View.VISIBLE
            }
            onColorListener!!.onClick(position)
        }
    }
    private var onColorListener: ItemClickListener? = null
    fun setOnColorListener(onColorListener: ItemClickListener?) {
        this.onColorListener = onColorListener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
         val colorImage: RelativeLayout = itemView.findViewById(R.id.rv_color)
         val ivColorSelected:ImageView = itemView.findViewById(R.id.iv_color_selected)



    }
}
