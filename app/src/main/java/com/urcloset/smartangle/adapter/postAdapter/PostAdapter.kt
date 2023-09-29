package com.urcloset.smartangle.adapter.postAdapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.activity.sellerActivity.SellerActivity
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.*
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PostAdapter():RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private var posts:ArrayList<PostsModel.Data.Post>?=null
    private var context: Context?=null
    constructor(context: Context, items:ArrayList<PostsModel.Data.Post>) : this() {

        this.context=context
        this.posts=items

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.post_item, parent, false)
        )
    }
    var savedBefore:Boolean=false

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       var imageIndex =0
        var operationType:String="1"
        val post = posts?.get(position)
        if(!TemplateActivity.loginResponse?.data?.accessToken.isNullOrEmpty()) {
            savedBefore = post?.savedBefore!!
            holder.bookmark.visibility = View.VISIBLE
            if(savedBefore)
                holder.bookmark.setImageResource(R.drawable.ic_bookmark_red_svg)
            else holder.bookmark.setImageResource(R.drawable.ic_book_mark_svg_grey)

        }
        else holder.bookmark.visibility = View.GONE
        holder.tvTitle.text = post?.name!!
        holder.tvPrice.text = post.price.toString()+context?.getString(R.string.currency)
        holder.tvViews.text = post.views.toString()
        holder.tvDes.text = post.description!!
        holder.tvRateCount.text = "("+post.owner?.countRaters.toString()+")"
        holder.tvUsername.text = post.owner?.name
        holder.bookmark.setOnClickListener {
            if(!TemplateActivity.loginResponse?.data?.accessToken.isNullOrEmpty())

            if(post.savedBefore!!) {
                savedBefore = true
                operationType ="0"
            }
            else {
                savedBefore = false
                operationType ="1"

            }
            bookMarkProduct(post.id.toString(),operationType,holder)
        }
        setUpRateStars(post.owner?.rate!!,holder.star1,holder.star2,holder.star3,holder.star4,holder.star5)
        if(!post.owner.image.isNullOrEmpty())
            loadCircleImage(holder.ivAvatar,post.owner.fullPath.toString())
         handleImageSwitcher(holder,post,imageIndex)
        if(imageIndex<post.itemMedia!!.size)
        if(!post.itemMedia.get(imageIndex)?.mediaPath.isNullOrEmpty())
            loadImage(holder.ivMain,BasicTools.getUrlHttpImg(context!!,post.itemMedia.get(0)?.mediaPath!!))
        holder.next.setOnClickListener {
            if(imageIndex<post.itemMedia.size-1){
                imageIndex++
                loadAndHandleImageSwitcher(holder,post,imageIndex)


            }

        }
        holder.prev.setOnClickListener {
            if (imageIndex >0) {
                imageIndex--
                loadAndHandleImageSwitcher(holder,post,imageIndex)



            }
        }
        holder.userSection.setOnClickListener {
            if(!post.owner.id.toString().isNullOrEmpty()) {
                val intent = Intent(context, SellerActivity::class.java)
                intent.putExtra("identifier", post.owner.id.toString())
                context?.startActivity(intent)
            }
        }
        holder.ivMain.setOnClickListener {
            val intent = Intent(context, ProductDetails::class.java)
            val gson = Gson()
            intent.putExtra("product", gson.toJson(post))
            context?.startActivity(intent)

        }
        holder.share.setOnClickListener {
            if (!post.name.isNullOrEmpty() &&
                !post.owner.name.isNullOrEmpty() &&
                !post.price.toString().isEmpty()
            ) {
                context?.let { it1 ->
                    BasicTools.shareProduct(
                        name = post.name,
                        owner = post.owner.name.toString(),
                        price = post.price.toString(),
                        id = post.id.toString(),
                        context = it1,
                        image = post.itemMedia[0]?.mediaPath

                    )
                }
            }
            else {
                (context as TemplateActivity).showToastMessage(R.string.faild)
            }
        }




    }
    fun addNewItem(item: PostsModel.Data.Post){
        posts?.add(item)
      //  notifyItemInserted(posts?.size!!-1)

    }
    fun loadAndHandleImageSwitcher(holder: ViewHolder, post:PostsModel.Data.Post, imageIndex:Int){
        loadImage(holder.ivMain,post.itemMedia?.get(imageIndex)?.fullPath!!)
        handleImageSwitcher(holder,post,imageIndex)



    }
    fun clearPosts(){
        posts?.clear()
        notifyDataSetChanged()
    }
    fun loadImage(image:ImageView,path:String){


        BasicTools.loadImage(path,image,object : DownloadListener {
            override fun completed(status: Boolean, bitmap: Bitmap) {
            /*   val anim = AnimationUtils.loadAnimation(context?.applicationContext, android.R.anim.fade_in)
                anim!!.setDuration(1000)
                image.animation = anim*/
            }
        })

    }
    fun loadCircleImage(image:ImageView,path:String){

        BasicTools.loadImage(path,image,object : DownloadListener {
            override fun completed(status: Boolean, bitmap: Bitmap) {
            /*    val anim = AnimationUtils.loadAnimation(context?.applicationContext, android.R.anim.fade_in)
                anim!!.setDuration(1000)
                image.animation = anim*/
            }
        })

    }
    fun handleImageSwitcher(holder: ViewHolder, post:PostsModel.Data.Post, imageIndex:Int){
        if(imageIndex==0)
            holder.prev.visibility = View.GONE
        else  holder.prev.visibility = View.VISIBLE

        if(imageIndex==post.itemMedia?.size!!-1)
            holder.next.visibility = View.GONE
        else  holder.next.visibility = View.VISIBLE

    }
    fun setUpRateStars(rated: Double,star1:ImageView,star2:ImageView,star3:ImageView,star4:ImageView,star5:ImageView) {
        val rate: Int = rated.toInt()
        if (rate == 0) {

        } else if (rate == 1) {
            star1.setImageResource(R.drawable.ic_star_filled)

        } else if (rate == 2) {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)


        } else if (rate == 3) {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)


        } else if (rate == 4) {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)
            star4.setImageResource(R.drawable.ic_star_filled)
            star5.setImageResource(R.drawable.ic_star_filled)
        } else if (rate == 5) {
            star1.setImageResource(R.drawable.ic_star_filled)
            star2.setImageResource(R.drawable.ic_star_filled)
            star3.setImageResource(R.drawable.ic_star_filled)
            star4.setImageResource(R.drawable.ic_star_filled)
            star5.setImageResource(R.drawable.ic_star_filled)
        }
    }
    fun bookMarkProduct(id:String,operationType:String,holder: ViewHolder){
        val disposable = CompositeDisposable()

        beginBookShimmer(holder)

        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(holder.itemView.context).toString(), "en"
            )
                ?.create(
                    AppApi::class.java
                )
        val map = HashMap<String, String>()
        map.put("operation_type", operationType)
        map.put("model_id", id)
        map.put("model_name", "Items")
        val observable = shopApi!!.saveProduct(map)
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<BasicModel>(holder.itemView.context) {
                    override fun onSuccess(result: BasicModel) {
                        if (result.status!!) {
                            endBookShimmer(holder)
                            if (operationType == "1") {
                                savedBefore = true
                                holder.bookmark.setImageResource(R.drawable.ic_bookmark_red_svg)

                                Toast.makeText(
                                    holder.itemView.context,
                                    holder.itemView.context.resources.getString(R.string.add_to_favourite_msg),
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                savedBefore = false
                                holder.bookmark.setImageResource(R.drawable.ic_book_mark_svg_grey)

                                Toast.makeText(
                                    holder.itemView.context,
                                    result.messages,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }


                        }


                    }

                    override fun onFailed(status: Int) {
                        endBookShimmer(holder)

                    }
                })
        )

    }

    fun beginBookShimmer(holder: ViewHolder){
        holder.bookmark.visibility = View.GONE
        holder.shimmerBookmark.visibility = View.VISIBLE
        holder.shimmerBookmark.startShimmerAnimation()

    }
    fun endBookShimmer(holder: ViewHolder){
        holder.shimmerBookmark.stopShimmerAnimation()
        holder.shimmerBookmark.visibility = View.GONE
        holder.bookmark.visibility = View.VISIBLE




    }


    override fun getItemCount(): Int {
        return posts!!.size
    }
    class ViewHolder(val view: View):RecyclerView.ViewHolder(view){
        val ivAvatar:ImageView = view.findViewById(R.id.iv_avatar)
        val tvUsername:TextView=view.findViewById(R.id.tv_username)
        val tvRateCount :TextView=view.findViewById(R.id.tv_rate_count)
        val tvTitle :TextView=view.findViewById(R.id.tv_title)
        val tvDes :TextView=view.findViewById(R.id.tv_des)
        val ivMain:ImageView=view.findViewById(R.id.iv_main)
        val tvPrice:TextView=view.findViewById(R.id.tv_price)
        val tvViews:TextView=view.findViewById(R.id.tv_views)
        val next:ImageView=view.findViewById(R.id.iv_next)
        val prev:ImageView=view.findViewById(R.id.iv_prev)
        val star1:ImageView=view.findViewById(R.id.iv_star_1)
        val star2:ImageView=view.findViewById(R.id.iv_star_2)
        val star3:ImageView=view.findViewById(R.id.iv_star_3)
        val star4:ImageView=view.findViewById(R.id.iv_star_4)
        val star5:ImageView=view.findViewById(R.id.iv_star_5)
        val bookmark:ImageView=view.findViewById(R.id.iv_bookmark)
        val share:ImageView=view.findViewById(R.id.iv_share)
        val shimmerBookmark:ShimmerFrameLayout=view.findViewById(R.id.shimmer_book_mark)
        val userSection:LinearLayout=view.findViewById(R.id.user_section)









    }
}