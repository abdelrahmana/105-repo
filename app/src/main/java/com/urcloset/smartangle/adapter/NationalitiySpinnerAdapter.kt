package com.urcloset.smartangle.adapter




import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.model.NationalityModel
import com.urcloset.smartangle.tools.BasicTools



class NationalitiySpinnerAdapter(
    val context: Context,
    var dataList: ArrayList<NationalityModel.DataArray>
) : BaseAdapter() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val vh: ItemRowHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.myspinner_text, parent, false)
            vh = ItemRowHolder(view)
            view?.tag = vh
        } else {
            view = convertView
            vh = view.tag as ItemRowHolder
        }

        // setting adapter item height programatically.

        /*  val params = view.layoutParams
        params.height = 60
        view.layoutParams = params*/

        if(BasicTools.isDeviceLanEn())
        vh.label.text = dataList.get(position).enTitle.toString()
        else
            vh.label.text = dataList.get(position).arTitle.toString()
        return view
    }

    override fun getItem(position: Int): Any? {

        return dataList.get(position)

    }

    override fun getItemId(position: Int): Long {

        return 0

    }

    override fun getCount(): Int {
        return dataList.size
    }

    private class ItemRowHolder(row: View?) {

        val label: TextView

        init {
            this.label = row?.findViewById(android.R.id.text1) as TextView
        }
    }
}


