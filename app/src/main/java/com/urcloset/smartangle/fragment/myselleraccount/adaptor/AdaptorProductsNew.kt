package com.urcloset.smartangle.fragment.myselleraccount.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.urcloset.shop.tools.show
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.ProductOneItemBinding
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools


class AdaptorProductsNew( // one selection
    var context: Context,
    var arrayList: ArrayList<ProductModel.Product>,
    val isOwner: Boolean,
    val callBackMoreOption: (Triple<Int, ProductModel.Product, View>) -> Unit,
    val callBackProductDetails: (ProductModel.Product) -> Unit
    // var selectedArrayList: ArrayList<ModelTrip>
) :
    RecyclerView.Adapter<AdaptorProductsNew.ViewHolder>() {

    fun setArrayListNew(arrayList: ArrayList<ProductModel.Product>) {
        this.arrayList.also { it.clear()
        it.addAll(arrayList)}
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = ProductOneItemBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(
            binding
        )

    }

    override fun getItemCount(): Int {

        return arrayList.size

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(arrayList[position])

    }
   inner class ViewHolder(val itemViews: ProductOneItemBinding) : RecyclerView.ViewHolder(itemViews.root) {
       fun bindItems(
           selectedItem: ProductModel.Product?
       ) {
           itemViews.cardMoreOption.show(isOwner)
           itemViews.numberOfViews.text = (selectedItem?.views?:0).toString()
           itemViews.nameOfProduct.text = selectedItem?.name?:""
           itemViews.priceText.text = (selectedItem?.price.toString()) + context.getString(R.string.currency)
           Glide.with(context.applicationContext).load(
               BasicTools.getUrlHttpImg(
                   context, selectedItem?.itemMedia?.get(
                       0
                   )?.mediaPath!!
               )).into(itemViews.productImage)
           itemViews.cardMoreOption.setOnClickListener{
               callBackMoreOption.invoke(Triple(adapterPosition,selectedItem,itemViews.cardMoreOption))
           }
           itemViews.productContainerConstrain.setOnClickListener{
               callBackProductDetails.invoke(selectedItem)
           }
       }


   }

}