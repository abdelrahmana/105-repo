package com.urcloset.smartangle.adapter.project105

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.urcloset.smartangle.R
import com.urcloset.smartangle.model.project21.JobTitleModel
import com.urcloset.smartangle.model.project_105.CountryModel
import com.urcloset.smartangle.model.project_105.CountryWithCity
import com.urcloset.smartangle.tools.BasicTools


class CountryWithCitySpinnerAdapter(
    context: Context,
    // Initialise custom font, for example:
    //    Typeface font = Typeface.createFromAsset(getContext().getAssets(),
    //            "custom_font.ttf");
    //    Typeface mTypeface;

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    private var mItems: ArrayList<CountryWithCity.Data.Citty>
)//        mTypeface=typeface;
    : ArrayAdapter<CountryWithCity.Data.Citty>(context, R.layout.spinner_text_item_small, mItems) {

    // Affects default (closed) state of the spinner
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent) as TextView
//        Utility.setGravityForTextView(context, view)
        setItemTextColor(view, position, false)
        if(BasicTools.isDeviceLanEn())
       view.setText(mItems.get(position).name?.toString())
        else
            view.setText(mItems.get(position).nameAr?.toString())
        //        view.setTypeface(mTypeface);
        return view
    }

    // Affects opened state of the spinner
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent) as TextView
//        Utility.setGravityForTextView(context, view)
        setItemTextColor(view, position, true)

        if(BasicTools.isDeviceLanEn())
            view.setText(mItems.get(position).name?.toString())
        else
            view.setText(mItems.get(position).nameAr?.toString())
        //        view.setTypeface(mTypeface);
        return view
    }

    override fun getCount(): Int {
        return mItems!!.size
    }

    fun SwapData(newData: List<String>) {

        notifyDataSetChanged()
    }


    private fun getColor(colorResId: Int): Int {
        return ContextCompat.getColor(context, colorResId)
    }
    fun clearList() {
        mItems.clear()
        notifyDataSetChanged()
    }

    private fun setItemTextColor(tv: TextView, position: Int, isDropDown: Boolean) {
     /*   if(position==0){
            tv.setTextColor(getColor(R.color.spinner_item_gray_selected))
        }else{
            tv.setTextColor(getColor(android.R.color.black))
        }*/

//        if (position == 0) {
//            if (isDropDown) {
//                tv.setTextColor(getColor(R.color.spinner_item_gray_selected))
//            } else {
//                tv.setTextColor(getColor(R.color.spinner_item_gray_unselected))
//            }
//        } else {
//            if (isDropDown) {
//                tv.setTextColor(getColor(android.R.color.black))
//
//            } else {
//                tv.setTextColor(getColor(android.R.color.white))
//
//            }
//        }
    }

}