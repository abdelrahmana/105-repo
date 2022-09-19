package com.urcloset.smartangle.adapter
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productImagesActivity.OnImageDownloadListen
import com.urcloset.smartangle.globals.Global
import com.urcloset.smartangle.model.ProductDetailsModel
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener
import com.urcloset.smartangle.tools.TemplateActivity


class ProductImagesAdapter(val images:ArrayList<ProductDetailsModel.Data.Item.ItemMedia>, val context:Context): PagerAdapter() {
    lateinit var layoutInflater :LayoutInflater
    lateinit var productImage:ImageView
    lateinit var saveImage:ImageView
    lateinit var back:ImageView
     var onImageDownloadStart: OnImageDownloadListen?=null
    var firstLoad = true



    fun setOnImageDownload(onImageDownload: OnImageDownloadListen? ){
    this.onImageDownloadStart = onImageDownload

}

    override fun getCount(): Int {
        return images.size



    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object` as RelativeLayout

    }



    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View= layoutInflater.inflate(R.layout.product_images_adapter,container,false)
        productImage = view.findViewById(R.id.product_image)
        saveImage = view.findViewById(R.id.iv_download)
        back = view.findViewById(R.id.iv_back)
        back.setOnClickListener {
            BasicTools.exitActivity(context as TemplateActivity)

        }
        saveImage.setOnClickListener {
            if(images[position].mediaPath!=null && !images[position].itemId.toString().isNullOrEmpty() && !images[position].mediaType.toString().isNullOrEmpty())
            this.onImageDownloadStart?.onClick(images[position].mediaPath!!,images[position].itemId.toString(),images[position].mediaType!!)

        }
            BasicTools.loadImage(BasicTools.getUrlHttpImg(
                context,
                images[position].mediaPath!!
            ),
                productImage, object : DownloadListener {
                    override fun completed(status: Boolean, bitmap: Bitmap) {
                        val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                        anim!!.setDuration(1000)
                    }
                })




        container.addView(view)
        return view
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}