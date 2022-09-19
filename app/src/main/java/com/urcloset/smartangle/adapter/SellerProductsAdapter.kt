package com.urcloset.smartangle.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.model.CategoryModel
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.model.UserProfileModel
import com.urcloset.smartangle.model.UsersModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener
import com.urcloset.smartangle.tools.TemplateActivity

class SellerProductsAdapter(
    val products: ArrayList<ArrayList<ProductModel.Product>>,
    val context: Context,
    val categories: List<CategoryModel.Category>,
): PagerAdapter() {
    lateinit var layoutInflater :LayoutInflater
    lateinit var ivEmptyProduct:ImageView
    lateinit var mainProductImage:ImageView
    lateinit var ivPrev:ImageView
    lateinit var ivNext:ImageView
    lateinit var tvPrice:TextView
    lateinit var tvViews:TextView
     var  selectedProduct:Int = 0


    override fun getCount(): Int {
        return products.size



    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object` as RelativeLayout

    }
    public fun reset(){
        selectedProduct = 0
        notifyDataSetChanged()

    }



    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View= layoutInflater.inflate(
            R.layout.category_products_seller_layout,
            container,
            false
        )
        mainProductImage = view.findViewById(R.id.main_product_image)
        ivPrev = view.findViewById(R.id.iv_prev)
        ivNext = view.findViewById(R.id.iv_next)
        tvPrice = view.findViewById(R.id.tv_price)
        tvViews = view.findViewById(R.id.tv_views)
        ivEmptyProduct = view.findViewById(R.id.empty_products)
        if(products[position].size>0){
            ivEmptyProduct.visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.ly_pr).visibility = View.VISIBLE
        }
        else{
            ivEmptyProduct.visibility = View.VISIBLE
            view.findViewById<LinearLayout>(R.id.ly_pr).visibility = View.GONE
        }



        if(selectedProduct<=products[position].size-1) {
            val product = products[position][selectedProduct]

            if (!product.itemMedia.isNullOrEmpty())
                if(product.itemMedia.get(0)?.mediaPath!=null)
                    Glide.with(context.applicationContext).load(BasicTools.getUrlHttpImg(context,product.itemMedia.get(0)?.mediaPath!!))
                        .into(mainProductImage)
            tvPrice.setText(product.price.toString()+context.getString(R.string.currency))
            tvViews.setText(product.views.toString())
            if (products[position].size == 0) {
                ivEmptyProduct.visibility = View.VISIBLE
            }
            mainProductImage.setOnClickListener {



                val intent = Intent(context, ProductDetails::class.java)
                val gson = Gson()
                intent.putExtra("product", gson.toJson(product))
                context.startActivity(intent)
            }
            checkProducs(position)

        }

            ivNext.setOnClickListener {
                if(selectedProduct<products[position].size-1) {
                    selectedProduct++
                    val nextProduct = products[position][selectedProduct]
                    if (!nextProduct.itemMedia.isNullOrEmpty())
                        Glide.with(context).load(BasicTools.getUrlHttpImg(context,nextProduct.itemMedia.get(0)?.mediaPath!!))
                            .into(mainProductImage)
                    tvPrice.setText(nextProduct.price.toString()+context.getString(R.string.currency))
                    tvViews.setText(nextProduct.views.toString())
                    checkProducs(position)
                    notifyDataSetChanged()
                }



            }
            ivPrev.setOnClickListener {
                if(selectedProduct>0) {
                    selectedProduct--
                    if(selectedProduct<=products[position].size-1) {
                        val prevProduct = products[position][selectedProduct]
                        if (!prevProduct.itemMedia.isNullOrEmpty())
                            if(prevProduct.itemMedia.get(0)?.mediaPath!=null)
                                Glide.with(context).load(BasicTools.getUrlHttpImg(context,prevProduct.itemMedia.get(0)?.mediaPath!!))
                                    .into(mainProductImage)

                        tvPrice.setText(prevProduct.price.toString()+context.getString(R.string.currency))
                        tvViews.setText(prevProduct.views.toString())
                        checkProducs(position)
                        notifyDataSetChanged()
                    }


                }


            }


        container.addView(view)
        return view
    }
    fun checkProducs(position: Int){
        if(selectedProduct < products[position].size-1)
            ivNext.visibility = View.VISIBLE
        else ivNext.visibility = View.INVISIBLE
        if(selectedProduct > 0)
            ivPrev.visibility = View.VISIBLE
        else ivPrev.visibility = View.INVISIBLE

    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
}