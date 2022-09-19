package com.urcloset.smartangle.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.listeners.OnProductChange
import com.urcloset.smartangle.model.CategoryModel
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity


class MySellerProductsAdapter(
    val products: ArrayList<ArrayList<ProductModel.Product>>,
    val context: Context,
    val categories: List<CategoryModel.Category>
): PagerAdapter()  {
    lateinit var layoutInflater : LayoutInflater
    lateinit var ivEmptyProduct: ImageView
    lateinit var mainProductImage:ImageView
    lateinit var ivPrev:ImageView
    lateinit var ivNext:ImageView
    lateinit var tvPrice: TextView
    lateinit var tvViews: TextView
    var  selectedProduct:Int = 0
    lateinit var onProductChangeListener: OnProductChange
    lateinit var root:ViewGroup

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
    fun setOnProductChangeListen(onProductChange: OnProductChange){
        this.onProductChangeListener =onProductChange

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(
            R.layout.my_category_products_seller_layout,
            container,
            false
        )
        root = container
        mainProductImage = view.findViewById(R.id.main_product_image)
        tvPrice = view.findViewById(R.id.tv_price)
        tvViews = view.findViewById(R.id.tv_views)
        ivPrev = view.findViewById(R.id.iv_prev)
        ivNext = view.findViewById(R.id.iv_next)
        ivEmptyProduct = view.findViewById(R.id.empty_products)
        if(products[position].size<=0)

            hideController(view)


        if(selectedProduct<=products[position].size-1) {
            val product = products[position][selectedProduct]
            mainProductImage.setOnClickListener {
                val intent = Intent(context, ProductDetails::class.java)
                val gson = Gson()
                intent.putExtra("product", gson.toJson(product))
                context.startActivity(intent)
            }

            if (!product.itemMedia.isNullOrEmpty())
                if(product.itemMedia.get(0)?.mediaPath!=null)
                    Glide.with(context.applicationContext).load(
                        BasicTools.getUrlHttpImg(
                            context, product.itemMedia.get(
                                0
                            )?.mediaPath!!
                        )
                    )
                        .into(mainProductImage)
            tvPrice.setText(product.price.toString() + context.getString(R.string.currency))
            tvViews.setText(product.views.toString())
            if (products[position].size == 0) {
                ivEmptyProduct.visibility = View.VISIBLE
            }
        }
        checkProducs(position)
        ivNext.setOnClickListener {
            if(selectedProduct<products[position].size-1) {
                selectedProduct++
                val nextProduct = products[position][selectedProduct]
                if (!nextProduct.itemMedia.isNullOrEmpty())
                    Glide.with(context).load(
                        BasicTools.getUrlHttpImg(
                            context, nextProduct.itemMedia.get(
                                0
                            )?.mediaPath!!
                        )
                    )
                        .into(mainProductImage)
                tvPrice.setText(nextProduct.price.toString() + context.getString(R.string.currency))
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
                            Glide.with(context).load(
                                BasicTools.getUrlHttpImg(
                                    context, prevProduct.itemMedia.get(
                                        0
                                    )?.mediaPath!!
                                )
                            )
                                .into(mainProductImage)

                    tvPrice.setText(prevProduct.price.toString() + context.getString(R.string.currency))
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
    fun setOnProductChangeState(onProductChange: OnProductChange){
        this.onProductChange = onProductChange

    }
  lateinit  var onProductChange:OnProductChange

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }
   fun hideController(view :View) {
       ivEmptyProduct.visibility = View.VISIBLE
       view.findViewById<LinearLayout>(R.id.ly_pr).visibility = View.GONE

   }

}