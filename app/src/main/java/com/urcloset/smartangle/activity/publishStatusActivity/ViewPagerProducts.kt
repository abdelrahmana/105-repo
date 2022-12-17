package com.urcloset.smartangle.activity.publishStatusActivity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerProducts(fragment: FragmentActivity, val arrayList:List<InterfacePublication>)
    : FragmentStateAdapter(fragment)  {

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun createFragment(position: Int): Fragment {
        val productImplementer = arrayList.get(position)
        val fragment  = ProductOneItemFragment(productImplementer)
     //   orderFragment.arguments =   bundleOf(SELECTED_ITEM to arrayList.get(position).id)
     /*   val fragment = PopUPServiceDialog()
        val bundle = Bundle()
        bundle.putBoolean(PopUPServiceDialog.IS_VIDEO, arrayList.get(position).mediaFile.contains(".mp4"))
        bundle.putString(PopUPServiceDialog.URLIMAGEVIDEO,arrayList.get(position).mediaFile)
        bundle.putString(PopUpImageVideoPager.MEDIA_LIST,Gson().toJson(arrayList))

        fragment.arguments = bundle
        return fragment!!*/
        return fragment

    }



}








