package com.urcloset.smartangle.fragment.HomeFragment.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.sellerActivity.SellerActivity
import com.urcloset.smartangle.databinding.UserAdaptorItemBinding
import com.urcloset.smartangle.model.NearbyUsersModel
import com.urcloset.smartangle.tools.BasicTools

class UsersAdaptorList(
    val imageClickedLUnit: ((NearbyUsersModel.Data.User) -> Unit)?
    , val arrayList: ArrayList<NearbyUsersModel.Data.User>
) : RecyclerView.Adapter<UsersAdaptorList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = UserAdaptorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    //    holder.bindItems(imageModel.image!![position],/*modelData*/imageModel.image!!)
        holder.bindItems(arrayList[holder.adapterPosition])

        //  setAnimation(holder.itemView, position)


    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return  arrayList.size
    }
    /*fun updateList(data: List<DataX>) {
        if (arrayListOfImagessValues?.isEmpty()==true) {
            arrayListOfImagessValues?.addAll(data)
            notifyDataSetChanged()
        } else {
            arrayListOfImagessValues?.addAll(data)
            notifyItemRangeInserted(0, arrayListOfImagessValues!!.size - 1)
        }
    }*/
    fun updateList(data: List<NearbyUsersModel.Data.User>) { // update loader when scroll
        if (arrayList?.isEmpty()==true) {
            arrayList?.addAll(data)
            notifyDataSetChanged()
        } else {
            val prevSize = arrayList.size
            arrayList?.addAll(data)
            notifyItemRangeChanged(0,data.size)
        }
    }

    //the class is hodling the list view
    inner class ViewHolder(val imageItemadaptorBinding: UserAdaptorItemBinding) :
        RecyclerView.ViewHolder(imageItemadaptorBinding.root) {


        fun bindItems(
                itemData: NearbyUsersModel.Data.User
        ) {

        //    imageItemadaptorBinding.userName.text = itemData.name?:""
            Glide.with(imageItemadaptorBinding.ivUserImg1.context).load( itemData.fullPath/*BasicTools.getUrlHttpImg(
                imageItemadaptorBinding.ivUserImg1.context,
                itemData.fullPath?:""
            )*/).circleCrop()
                .apply( RequestOptions().override(200, 200))
                .error(R.color.background_image).placeholder(R.color.background_image).dontAnimate()
                .into(imageItemadaptorBinding.ivUserImg1)
            // set work status
            //imageItemadaptorBinding.price.text = itemData.?:""
            imageItemadaptorBinding.tvTitle1.text = (itemData.name)
           // imageItemadaptorBinding.tvTitle1.text = (itemData.name)

            imageItemadaptorBinding.visit1.setOnClickListener {
                val intent = Intent(imageItemadaptorBinding.visit1.context, SellerActivity::class.java)
                intent.putExtra("identifier", itemData.id.toString())
                imageItemadaptorBinding.visit1.context.startActivity(intent)
            }
        }


    }

//    var defaultSelectedItem = -1 // defqult no thing selected
}
//class ClickAction(item : Datax)