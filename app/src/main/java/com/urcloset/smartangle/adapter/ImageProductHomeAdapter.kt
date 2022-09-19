package com.urcloset.smartangle.adapter


import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView


import androidx.viewpager.widget.PagerAdapter
import com.urcloset.smartangle.R


class ImageProductHomeAdapter(var context: Context?, var dataList: ArrayList<Bitmap>) : PagerAdapter() {
    private var layoutInflater: LayoutInflater? = null
    private var anim: Animation? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater =
            context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = layoutInflater!!.inflate(R.layout.card_image, container, false)
        var image=view.findViewById(R.id.iv_product)as ImageView

        anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        anim!!.setDuration(3000)
        image.animation= anim
        image.setImageBitmap(dataList.get(position))
        //var item=dataList.get(position).id
        //var urlLink=BasicTools.getUrlForImage(context!!,item.toString())
     /*   var image=view.findViewById(R.id.iv_product) as ImageView

        BasicTools.loadImage(urlLink,image,object :DownloadListener{
            override fun completed(status: Boolean, loadedImage: Bitmap) {

            }
        })*/




        container.addView(view)

        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

    override fun getCount(): Int {
        return dataList.size
    }
}