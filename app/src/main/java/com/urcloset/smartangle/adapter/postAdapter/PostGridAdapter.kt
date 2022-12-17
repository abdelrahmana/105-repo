package com.urcloset.smartangle.adapter.postAdapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.databinding.PostGridItemBinding
import com.urcloset.smartangle.model.*
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener




class PostGridAdapter():RecyclerView.Adapter<PostGridAdapter.ViewHolder>() {
    private var posts:ArrayList<PostsModel.Data.Post>?=null
    private var context: Context?=null
    private var anim: Animation?=null
    constructor(context: Context, items: ArrayList<PostsModel.Data.Post>) : this() {

        this.context=context
        this.posts=items
         anim = ScaleAnimation(
             0.2f, 1f,  // Start and end values for the X axis scaling
             0.2f, 1f,  // Start and end values for the Y axis scaling
             Animation.RELATIVE_TO_SELF, 0.5f,  // Pivot point of X scaling
             Animation.RELATIVE_TO_SELF, 0.5f
         ) // Pivot point of Y scaling

        anim?.fillAfter = true // Needed to keep the result of the animation

        anim?.duration = 1000

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding : PostGridItemBinding = PostGridItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(
           /* LayoutInflater.from(parent.context)
                .inflate(R.layout.post_grid_item, parent, false)*/
        binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       // holder.itemView.startAnimation(anim)
      val post=posts?.get(position)
        if(post?.itemMedia?.size!! >0)
            if(!post.itemMedia.get(0)?.mediaPath.isNullOrEmpty()) {
                holder.ivPost?.transitionName = post.itemMedia.get(0)?.id.toString()
                loadImage(holder.ivPost,BasicTools.getUrlHttpImg(context!!,post.itemMedia.get(0)?.mediaPath!!))
                ViewCompat.setTransitionName(holder.ivPost!!,  "profile");


            }



        holder.cvMain.setOnClickListener {
            val intent = Intent(context, ProductDetails::class.java)
            val gson = Gson()
            intent.putExtra("product", gson.toJson(post))
            if(post.itemMedia.isNotEmpty())
                if(!post.itemMedia.get(0)?.mediaPath.isNullOrEmpty()) {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        context as Activity,
                        (holder.cvMain as View?)!!,  "profile"
                    )
                    context?.startActivity(intent, options.toBundle())
                }
        }



    }
    fun addNewItem(item: PostsModel.Data.Post){
        posts?.add(item)
        notifyItemInserted(posts?.size!! - 1)

    }

    fun loadImage(image: ImageView?, path: String){


        BasicTools.loadImage(path,image,object : DownloadListener {
            override fun completed(status: Boolean, bitmap: Bitmap) {
            /*    val anim = AnimationUtils.loadAnimation(context?.applicationContext, android.R.anim.fade_in)
                anim!!.setDuration(1000)
                image.animation = anim*/
            }
        })




    }





    override fun getItemCount(): Int {
        return posts!!.size
    }
    class ViewHolder(val view: PostGridItemBinding):RecyclerView.ViewHolder(view.root){
        val ivPost:ImageView? = view.ivImg//findViewById(R.id.iv_img)
        val cvMain:CardView = view.cvItem//findViewById(R.id.cv_item)











    }
}