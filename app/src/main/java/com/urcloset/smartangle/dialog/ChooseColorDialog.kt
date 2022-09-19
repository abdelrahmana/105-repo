package com.urcloset.smartangle.dialog


import android.app.Dialog
import android.view.Window
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.searchActivity.ISearchActivity
import com.urcloset.smartangle.adapter.project105.ChooseColorAdapter

import com.urcloset.smartangle.adapter.project105.SpacesItemDecorationV2
import com.urcloset.smartangle.model.project_105.ColorModelHassan

import com.urcloset.smartangle.tools.TemplateActivity


class ChooseColorDialog(val context: TemplateActivity,
                        var list:ArrayList<ColorModelHassan.Color>,
val iview: ISearchActivity
)
    : Dialog(context) {




    lateinit var ivBack:ImageView
    lateinit var rv:RecyclerView
    lateinit var btnOk:CardView

    lateinit var adapter: ChooseColorAdapter
    lateinit var layoutManager: GridLayoutManager



    init {


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_choose_color)
        initView()
        initEvent()


    }




    private fun initView() {
        btnOk=findViewById(R.id.card_ok)
        ivBack=findViewById(R.id.iv_clear_color)
        rv=findViewById(R.id.rv_choose_colors)
        adapter= ChooseColorAdapter(context, ArrayList())
        layoutManager=GridLayoutManager(context,4)
        rv.layoutManager=layoutManager
        rv.adapter=adapter
        adapter.change_data(list)

        val spacingInPixels2 =context.resources.getDimensionPixelSize(R.dimen.d0_2)
        val spacingInPixels3 =context.resources.getDimensionPixelSize(R.dimen.d0_4)
        rv.addItemDecoration( SpacesItemDecorationV2(spacingInPixels2,spacingInPixels3))




    }


    private fun initEvent() {


        ivBack.setOnClickListener{
            dismiss()

        }

        btnOk.setOnClickListener {

          var list=  adapter.getList()

            TemplateActivity.colorSearchList!!.clear()
            TemplateActivity.colorSearchList!!.addAll(list)

            iview.setColorData(list)
           this.dismiss()

        }
    }


















}