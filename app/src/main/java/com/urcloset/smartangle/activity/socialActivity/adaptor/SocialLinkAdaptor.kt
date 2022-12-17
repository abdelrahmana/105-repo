package com.urcloset.smartangle.activity.socialActivity.adaptor

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.databinding.OneItemGlobalLinksBinding
import com.urcloset.smartangle.model.Datax
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener

class SocialLinkAdaptor(val itemClickedAction : (Datax)->Unit,
                          //val itemAgreeAction : (Any)->Unit
    val arrayListOfImagessValues: ArrayList<Datax>,
                         // val canceled : (Pair<Any,Int>)->Unit, val utilii: Util,
) : RecyclerView.Adapter<SocialLinkAdaptor.ViewHolderGlobal>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderGlobal {
        val binding = OneItemGlobalLinksBinding.
        inflate(LayoutInflater.from(parent.context),parent,false)

        return ViewHolderGlobal(binding)
    }
    //this method is binding the data on the list
    override fun onBindViewHolder(holder:ViewHolderGlobal, position: Int) {
    //    holder.bindItems(imageModel.image!![position],/*modelData*/imageModel.image!!)
        holder.bindItems(arrayListOfImagessValues[position])

        //  setAnimation(holder.itemView, position)


    }
    inner class ViewHolderGlobal(
        val imageItemadaptorBinding: OneItemGlobalLinksBinding,
    ) : RecyclerView.ViewHolder(imageItemadaptorBinding.root)  {


         fun bindItems(
            itemData: Datax
        ) {
             BasicTools.loadImage(
                 itemData.full_path?:"",
                imageItemadaptorBinding.oneItemGlobal,null)
            imageItemadaptorBinding.oneItemGlobal.setOnClickListener{
                itemClickedAction.invoke(itemData)
            }
        }

    }
    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return  arrayListOfImagessValues.size
    }

}


