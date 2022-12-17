package com.urcloset.smartangle.fragment.unpaid.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.AdaptorOneItemSoldBinding
import com.urcloset.smartangle.databinding.UnpaidAdaptorLayoutBinding
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools


class AdaptorSold(
    var context: Context, var arrayList: ArrayList<ProductModel.Product>,
    val callBackInvoke: ((ArrayList<ProductModel.Product>) -> Unit)?
   // var selectedArrayList: ArrayList<ModelTrip>
) :
    RecyclerView.Adapter<AdaptorSold.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AdaptorOneItemSoldBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(
            binding
        )

    }
    fun getArrayListData(): ArrayList<ProductModel.Product> {
        return arrayList
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
   inner class ViewHolder(val itemViewData: AdaptorOneItemSoldBinding) :
       RecyclerView.ViewHolder(itemViewData.root) {
        fun bindItems(
            product: ProductModel.Product
        )
        {

            if (product.soldItem) {
             /*   itemViewData.checkedImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        itemViewData.cardImage.context,
                        R.drawable.ic_single_check
                    )
                )*/
                itemViewData.checkedImage.visibility = View.VISIBLE
                itemViewData.image.alpha = .7f

            }
            else {
                itemViewData.checkedImage.visibility = View.GONE
                itemViewData.image.alpha = 1f

            }
            Glide.with(itemViewData.image.context).load(product.itemMedia?.get(0)?.fullPath?:"")
                .error(R.color.background_image)/*.placeholder(R.color.background_image)*/.dontAnimate()
                .apply( RequestOptions().override(600, 600)).into(itemViewData.image)
            itemViewData.containerProduct.setOnClickListener{
                val newSelection = !arrayList.get(adapterPosition).soldItem
                arrayList.get(adapterPosition).soldItem = newSelection
                product.soldItem = newSelection
                notifyItemChanged(adapterPosition)
             //   val filteredArray = arrayList.filter { it.soldItem }
               // callBackInvoke.invoke(filteredArray as ArrayList<ProductModel.Product>)
            }

        }

    }



}