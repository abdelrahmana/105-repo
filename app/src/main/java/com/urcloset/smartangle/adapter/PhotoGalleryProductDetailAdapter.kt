package com.urcloset.smartangle.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productImagesActivity.ProductImagesActivity
import com.urcloset.smartangle.globals.Global
import com.urcloset.smartangle.model.ProductDetailsModel
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener


class PhotoGalleryProductDetailAdapter(
    val context: Context,
    val photos: ArrayList<ProductDetailsModel.Data.Item.ItemMedia>,
    val count: Int?
) : PagerAdapter(){

    lateinit var layoutInflater :LayoutInflater
    var index =0

    override fun getCount(): Int {
        return  count!!
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object` as LinearLayout

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View= layoutInflater.inflate(
            R.layout.product_detail_photo_gallery_layout,
            container,
            false
        )
        val imgMain = view.findViewById<ImageView>(R.id.iv_main)
        val imgTop = view.findViewById<ImageView>(R.id.iv_top)
        val imgDown = view.findViewById<ImageView>(R.id.iv_down)
        if(0+(3*index)<photos.size) {

                imgMain.transitionName =  "profile"
//            loadImage(
//                imgMain,
//                BasicTools.getUrlHttpImg(context, photos[0 + (3 * index)].mediaPath!!)
//            )

//               Picasso.get().load(BasicTools.getUrlHttpImg(context,photos[0 + (3 * index)].mediaPath!!))
//                   .networkPolicy(NetworkPolicy.OFFLINE)
//                .noFade()
//                .into(imgMain,object : Callback {
//                    override fun onSuccess() {
//                        (context as AppCompatActivity).supportStartPostponedEnterTransition();
//
//                    }
//
//                    override fun onError(e: Exception?) {
//
//                    }
//
//                    })
            (context as AppCompatActivity).supportPostponeEnterTransition()

            Glide.with(context)
                .load(BasicTools.getUrlHttpImg(context,photos[0 + (3 * index)].mediaPath!!))
                .centerCrop()
                .dontAnimate()
                .listener(object :RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        (context as AppCompatActivity).supportStartPostponedEnterTransition();
                        return false;                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        (context as AppCompatActivity). supportStartPostponedEnterTransition();
                        return false;
                    }

                })
                .into(imgMain)

        }
        if(1+(3*index)<photos.size) {

            BasicTools.loadImage(
                BasicTools.getUrlHttpImg(
                    context,
                    photos[1 + (3 * index)].mediaPath!!
                ),
                imgTop, object : DownloadListener {
                    override fun completed(status: Boolean, bitmap: Bitmap) {
                        val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                        anim!!.setDuration(1000)
                    }
                })
        }
        if(2+(3*index)<photos.size) {

            BasicTools.loadImage(
                BasicTools.getUrlHttpImg(
                    context,
                    photos[2 + (3 * index)].mediaPath!!
                ),
                imgDown, object : DownloadListener {
                    override fun completed(status: Boolean, bitmap: Bitmap) {
                        val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
                        anim!!.setDuration(1000)
                    }
                })
        }
        index++
       imgMain.setOnClickListener {
           val i =  Intent(context, ProductImagesActivity::class.java)
           Global.productImages = photos
           Global.position = position
           context.startActivity(i)
       }
       imgTop.setOnClickListener {
           val i =  Intent(context, ProductImagesActivity::class.java)
           Global.productImages = photos
           Global.position = position+1

           context.startActivity(i)
       }
        imgDown.setOnClickListener {
            val i =  Intent(context, ProductImagesActivity::class.java)
            Global.productImages = photos
            Global.position = position+2

            context.startActivity(i)
        }
        container.addView(view)
        return view
    }
    fun loadImage(image: ImageView, path: String){
//        BasicTools.loadImageWithHero(context,path, image, object : DownloadListener {
//            override fun completed(status: Boolean, bitmap: Bitmap) {
//
//            }
//        })
//        (context as AppCompatActivity).supportPostponeEnterTransition()

//        Glide.with(context)
//            .load(path)
//            .centerCrop()
//            .dontAnimate()
//            .listener(object : RequestListener<String?, GlideDrawable?> {
//                fun onException(
//                    e: Exception?,
//                    model: String?,
//                    target: Target<GlideDrawable?>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    supportStartPostponedEnterTransition()
//                    return false
//                }
//
//                fun onResourceReady(
//                    resource: GlideDrawable?,
//                    model: String?,
//                    target: Target<GlideDrawable?>?,
//                    isFromMemoryCache: Boolean,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    supportStartPostponedEnterTransition()
//                    return false
//                }
//            })
//            .into<com.bumptech.glide.request.target.Target<Drawable>>(imageView)

    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}