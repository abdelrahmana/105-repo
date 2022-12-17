package com.urcloset.smartangle.fragment.unpaid

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.UnpaidAdaptorLayoutBinding
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools


class UnpaidAdaptorItems(
    var context: Context, var arrayList: ArrayList<ProductModel.Product>,
    val callBackInvoke :(ArrayList<ProductModel.Product>)->Unit
   // var selectedArrayList: ArrayList<ModelTrip>
) :
    RecyclerView.Adapter<UnpaidAdaptorItems.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UnpaidAdaptorLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(
            binding
        )

    }
    fun clearProduct () {
        arrayList?.clear()
    }
    fun updateList(data: List<ProductModel.Product>) {
        if (arrayList?.isEmpty()==true) {
            arrayList?.addAll(data)
            notifyDataSetChanged()
        } else {
            arrayList?.addAll(data)
            notifyItemRangeChanged(0, data!!.size)
        }
    }
    override fun getItemCount(): Int {

        return arrayList.size

    }

 /*   override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }*/

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(arrayList[holder.adapterPosition])

    }
   inner class ViewHolder(val itemViewData: UnpaidAdaptorLayoutBinding) :
       RecyclerView.ViewHolder(itemViewData.root) {
        fun bindItems(
            product: ProductModel.Product
        )
        {
           itemViewData.productName.text  = product.name.toString()
           itemViewData.dateValue.text = BasicTools.getStringDate(product.createdAt?:"")
            if (product.selectedItem) {
                itemViewData.checkedImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemViewData.cardImage.context,
                        R.drawable.ic_single_check
                    )
                )
                itemViewData.checkedImage.visibility = View.VISIBLE

            }
            else
                itemViewData.checkedImage.visibility = View.GONE
            Glide.with(itemViewData.image.context).load(product.itemMedia?.get(0)?.fullPath?:"")
                .error(R.color.background_image)/*.placeholder(R.color.background_image)*/.dontAnimate()
                .apply( RequestOptions().override(600, 600)).into(itemViewData.image)
            itemViewData.containerProduct.setOnClickListener{
                val newSelection = !arrayList.get(adapterPosition).selectedItem
                arrayList.get(adapterPosition).selectedItem = newSelection
                product.selectedItem = newSelection
                notifyItemChanged(adapterPosition)
                val filteredArray = arrayList.filter { it.selectedItem }
                callBackInvoke.invoke(filteredArray as ArrayList<ProductModel.Product>)
            }

        }

    }



}