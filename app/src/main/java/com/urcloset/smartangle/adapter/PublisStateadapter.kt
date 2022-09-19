package com.urcloset.smartangle.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.updateProduct.UpdateProductActivity
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.BasicModel
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*


class PublishStateAdapter(): RecyclerView.Adapter<PublishStateAdapter.ViewHolder>() {
    private var products:ArrayList<ProductModel.Product> ?= null
    private var context:Context ?=null
    constructor(context: Context, products:ArrayList<ProductModel.Product>) : this() {

        this.context=context
        this.products=products

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view : View= LayoutInflater.from(parent.context)
            .inflate(R.layout.publish_state_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products?.get(position)
        if(product?.itemMedia?.size!!>0)

        Glide.with(holder.itemView.context.applicationContext)
            .load(
                BasicTools.getUrlHttpImg(
                    holder.itemView.context,
                    product.itemMedia.get(0)!!.mediaPath!!
                ),
            ).into(holder.image)

        if(BasicTools.isDeviceLanEn()) {
            holder.message.text = product.publishMessageEn
            holder.state.text = product.currentPublishStatus?.nameEn


        }
        else    {
            holder.message.text = product.publishMessageAr
            holder.state.text = product.currentPublishStatus?.nameAr

        }
        holder.name.text = product.name.toString()

        if(product.currentPublishStatus?.statusValue==1) {
            holder.imagestate.setImageResource(R.drawable.in_review)
            holder.edit.visibility = View.GONE
            holder.rejectedActions.visibility = View.GONE
            holder.message.setTextColor(Color.parseColor("#ACACAC"))

        }
        if(product.currentPublishStatus?.statusValue==2) {
            holder.imagestate.setImageResource(R.drawable.published_icon)
            holder.edit.visibility = View.VISIBLE
            holder.rejectedActions.visibility = View.GONE
            holder.message.setTextColor(Color.parseColor("#ACACAC"))




        }
        if(product.currentPublishStatus?.statusValue==0) {
            holder.imagestate.setImageResource(R.drawable.in_rejected)
            holder.rejectedActions.visibility = View.VISIBLE
            holder.edit.visibility = View.GONE
            holder.message.setTextColor(Color.parseColor("#f9306E"))
        }
        holder.edit.setOnClickListener {
            val intent = Intent(context,UpdateProductActivity::class.java)
            val gson = Gson()
            intent.putExtra("product", gson.toJson(product))
            context?.startActivity(intent)

        }
        holder.cvEditandpublish.setOnClickListener {
            val intent = Intent(context,UpdateProductActivity::class.java)
            val gson = Gson()
            intent.putExtra("product", gson.toJson(product))
            context?.startActivity(intent)

        }
        holder.delete.setOnClickListener {
            deleteProduct(holder.shimmerDelete,holder.delete,product.id.toString(),position)

        }
        try {
            if (null != calculateDate(product)) {
                holder.lyEdit.visibility = View.VISIBLE
                holder.timeEditText.text = calculateDate(product)
                holder.edit.visibility = View.VISIBLE


            } else {
                holder.lyEdit.visibility = View.GONE
                holder.edit.visibility = View.GONE


            }
        }
        catch (e:Exception){
            holder.lyEdit.visibility = View.GONE
            holder.edit.visibility = View.GONE
        }


    }

    override fun getItemCount(): Int {
        return products?.size!!
    }
    fun calculateDate( product:ProductModel.Product):String?{
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault())
        dateFormatter.timeZone =TimeZone.getTimeZone("UTC")
        val createdDate = dateFormatter.parse(product.createdAt!!)
        val createdTimeCalender = Calendar.getInstance()
        createdTimeCalender.time = createdDate!!
        createdTimeCalender.add(Calendar.HOUR, 24)
        val currentDate = Calendar.getInstance()
        val diffDate = createdTimeCalender.time.time-currentDate.time.time
        val seconds: Long = diffDate / 1000
        var minutes: Long = seconds / 60
        val hours = minutes / 60

        if(hours in 1..24){
            if(minutes<0)
                minutes=minutes*-1
            return String.format("%sh %sm",minutes/60,minutes%60)

        }
        else return null



    }
    fun deleteProduct( shimmerDelete:ShimmerFrameLayout,delete:CardView,productId:String,position:Int){

        if(BasicTools.isConnected(context!!)) {
            BasicTools.showShimmer(delete,shimmerDelete,true)

            val lang = if(BasicTools.isDeviceLanEn()) "en" else "ar"
            val disposable = CompositeDisposable()
               val  shopApi =
                    ApiClient.getClientJwt(
                        TemplateActivity.loginResponse?.data?.accessToken.toString(),
                        BasicTools.getProtocol(context as TemplateActivity).toString(), lang)
                        ?.create(
                            AppApi::class.java
                        )
            val map = HashMap<String,String>()
            map.put("item_status","0")
            map.put("product_id",productId)



            val observable = shopApi!!.changeProductStatus(map)
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object :
                        AppObservable<BasicModel>(context as TemplateActivity) {
                        override fun onSuccess(result: BasicModel) {
                            if(result.status == true ){
                                products?.removeAt(position)
                                notifyItemRemoved(position)
                                Toast.makeText(context, result.messages, Toast.LENGTH_SHORT).show()

                            }

                        }

                        override fun onFailed(status: Int) {
                            Toast.makeText(context, (context as TemplateActivity).resources.getText(R.string.faild), Toast.LENGTH_SHORT).show()


                            super.onFailed(status)
                        }

                        override fun onComplete() {
                            BasicTools.showShimmer(delete,shimmerDelete,false)

                            super.onComplete()
                        }


                    })
            )
        }
        else {
            Toast.makeText(context, R.string.no_connection, Toast.LENGTH_SHORT).show()

        }

    }
    class ViewHolder(val view: View) :RecyclerView.ViewHolder(view){
        val image = view.findViewById<ImageView>(R.id.image)
        val name = view.findViewById<TextView>(R.id.name)
        val delete = view.findViewById<CardView>(R.id.cv_delete)
        val shimmerDelete = view.findViewById<ShimmerFrameLayout>(R.id.shimmer_delete)
        val message = view.findViewById<TextView>(R.id.tv_message)
        val state = view.findViewById<TextView>(R.id.tv_state)
        val imagestate = view.findViewById<ImageView>(R.id.iv_icon_state)
        val edit = view.findViewById<CardView>(R.id.cv_edit)
        val rejectedActions = view.findViewById<RelativeLayout>(R.id.rl_rejected_actions)
        val  cvEditandpublish = view.findViewById<CardView>(R.id.cv_editandpublish)
        val timeEditText = view.findViewById<TextView>(R.id.time_edit_text)
        val lyEdit = view.findViewById<LinearLayout>(R.id.ly_edit)




    }
}