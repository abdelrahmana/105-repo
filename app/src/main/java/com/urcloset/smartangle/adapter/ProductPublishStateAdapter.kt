package com.urcloset.smartangle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.urcloset.smartangle.R
import com.urcloset.smartangle.model.ProductModel

class ProductPublishStateAdapter(
    val context: Context,
    val inReview: ArrayList<ProductModel.Product>,
    val published: ArrayList<ProductModel.Product>,
    val rejected: ArrayList<ProductModel.Product>
) : PagerAdapter() {
    lateinit var layoutInflater : LayoutInflater
    lateinit var rvProducts: RecyclerView

    override fun getCount(): Int {
        return 3

    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(
            R.layout.product_publish_state_layout,
            container,
            false
        )
        rvProducts = view.findViewById(R.id.rv_products)
        rvProducts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        var products = ArrayList<ProductModel.Product>()
        if(position==0){
            products = published

        }
        else if(position==1){
            products = inReview


        }
        else if(position==2){
            products =rejected


        }
         val publishStateAdapter =PublishStateAdapter(context, products)
         rvProducts.adapter = publishStateAdapter

        publishStateAdapter.notifyDataSetChanged()
        if(products.size==0)
            view.findViewById<LinearLayout>(R.id.ly_empty).visibility = View.VISIBLE
        else             view.findViewById<LinearLayout>(R.id.ly_empty).visibility = View.GONE


        container.addView(view)

        return view
    }
    public fun updateProducts(){
        notifyDataSetChanged()


    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object` as RelativeLayout


    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }


    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE;
    }


}