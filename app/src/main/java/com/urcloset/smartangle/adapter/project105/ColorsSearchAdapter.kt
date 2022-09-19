package com.urcloset.smartangle.adapter.project105
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

class ColorsSearchAdapter() :ListAdapter<String, ColorsSearchAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem

    }

    override fun areContentsTheSame(
        oldItem: String,
        newItem: String
    ): Boolean {
        return oldItem == newItem
    }
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsSearchAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.color_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ColorsSearchAdapter.ViewHolder, position: Int) {
        holder.colorImage.background.setColorFilter(
            Color.parseColor(getItem(position)),
            PorterDuff.Mode.MULTIPLY
        )

 /*       if (getItem(position).isSelected!!>0) {
            holder.ivColorSelected.visibility = View.VISIBLE
        } else {

            holder.ivColorSelected.visibility = View.GONE
        }*/
        holder.colorImage.setOnClickListener {
            val color: String =
                currentList[position]

       /*     if (color.isSelected!!>0) {
                color.isSelected= 0
                holder.ivColorSelected.visibility = View.GONE
            } else {
                color.isSelected= 1
                holder.ivColorSelected.visibility = View.VISIBLE
            }*/
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
