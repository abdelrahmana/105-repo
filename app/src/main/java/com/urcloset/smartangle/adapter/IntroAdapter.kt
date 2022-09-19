package com.urcloset.smartangle.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productImagesActivity.ProductImagesActivity
import com.urcloset.smartangle.globals.Global
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener

class IntroAdapter(val context: Context):PagerAdapter() {
    var view:View?=null
    override fun getCount(): Int {
       return 3
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = null
        if(position==0)
         view = layoutInflater.inflate(R.layout.first_screen,container,false)
        else if(position==1)
            view = layoutInflater.inflate(R.layout.second_screen,container,false)
        else if(position==2)
            view = layoutInflater.inflate(R.layout.third_screen,container,false)


        container.addView(view)
        return view!!
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object` as RelativeLayout
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}