package com.urcloset.smartangle.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.urcloset.smartangle.R
import com.urcloset.smartangle.tools.BasicTools


class SpinnerAdapter(var ctx: Context, var images: ArrayList<String>, var names: ArrayList<String>)

    : ArrayAdapter<String>(ctx, R.layout.spinner_value_layout,R.id.tv_cat_name,names) {


    override fun getDropDownViewTheme(): Resources.Theme? {
        return super.getDropDownViewTheme()
    }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return  getCustomView(position, convertView, parent)!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)!!
    }
    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row: View = inflater.inflate(R.layout.spinner_value_layout, parent, false)
        val textView = row.findViewById<View>(R.id.tv_cat_name) as TextView
        val imageView: ImageView = row.findViewById<View>(R.id.iv_cat_icon) as ImageView
        if(position!=0) {
            textView.setText(names.get(position))
            Glide.with(ctx.applicationContext)
                .load(BasicTools.getUrlHttpImg(ctx, images.get(position)))
                .into(imageView)
        }
        else {
            if(BasicTools.isDeviceLanEn())
            textView.setText("all products")
            else  textView.setText("جميع المنتجات")
           imageView.setImageResource(R.drawable.ic_posts_primary)

        }
        return row
    }
}