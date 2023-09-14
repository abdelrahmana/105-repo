package com.urcloset.smartangle.fragment.historypayment.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.urcloset.smartangle.R
import com.urcloset.smartangle.data.model.paymenthistory.Data
import com.urcloset.smartangle.databinding.OneItemHistoryBinding
import com.urcloset.smartangle.databinding.UnpaidAdaptorLayoutBinding
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
class AdaptorOneItemHistory(
    var context: Context, var arrayList: ArrayList<Data>
   // var selectedArrayList: ArrayList<ModelTrip>
) :
    RecyclerView.Adapter<AdaptorOneItemHistory.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OneItemHistoryBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(
            binding
        )

    }
    fun clearProduct () {
        arrayList?.clear()
    }
    fun updateList(data: List<Data>) {
        if (arrayList.isEmpty() ==true) {
            arrayList.addAll(data)
            notifyDataSetChanged()
        } else {
            arrayList?.addAll(data)
            notifyItemRangeChanged(0, data!!.size)
        }
    }
    override fun getItemCount(): Int {

        return arrayList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(arrayList[holder.adapterPosition])

    }
   inner class ViewHolder(val itemViewData: OneItemHistoryBinding) :
       RecyclerView.ViewHolder(itemViewData.root) {
        fun bindItems(
            paymentHistory: Data
        )
        {
            itemViewData.date.text = BasicTools.getStringDate(paymentHistory.created_at?:"")
            itemViewData.successValue.text = paymentHistory.payment_status
            itemViewData.priceValue.text = (paymentHistory.amount)+" " +itemViewData.containerProduct.context.getString(R.string.sar)


        }

    }



}