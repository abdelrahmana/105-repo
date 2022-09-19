package com.urcloset.smartangle.dialog


import android.app.Dialog
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView

import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.addProductActivity.IAddProduct


import com.urcloset.smartangle.tools.TemplateActivity
import okhttp3.MultipartBody


class HintAddProductDialog(val context: TemplateActivity,
                          val images:ArrayList<MultipartBody.Part>,
                           val sizes:ArrayList<Int>,val
                           map:Map<String,String>,
                           val colors:ArrayList<Int>,
                          val iview:IAddProduct,
                           val message:String

)
    : Dialog(context) {





    lateinit var cvContinue:CardView





    init {


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.bottomsheet_layout_add_product)
        initView()
        initEvent()


    }




    private fun initView() {
        cvContinue=findViewById(R.id.cv_continue)
        findViewById<TextView>(R.id.tv_msg).setText(message)






    }


    private fun initEvent() {




        cvContinue.setOnClickListener {



            iview.addProductRQDialog(images,sizes,map,colors)
           this.dismiss()

        }
    }


















}