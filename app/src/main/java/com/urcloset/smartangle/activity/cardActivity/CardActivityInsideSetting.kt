package com.urcloset.smartangle.activity.cardActivity


import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.verification_code.EnterCodeActivity
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.DownloadListener
import com.urcloset.smartangle.tools.TemplateActivity

class CardActivityInsideSetting : TemplateActivity() {
    lateinit var btnNext: CardView
    lateinit var name: TextView
    lateinit var img: ImageView

    override fun set_layout() {
        setContentView(R.layout.activity_card)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {
        name=findViewById(R.id.tv_name)
        btnNext=findViewById(R.id.card_contineu)
        img=findViewById(R.id.iv_image)


    }

    override fun init_events() {


        name.setText(TemplateActivity.loginResponse?.data?.user?.name)
        btnNext.setOnClickListener {
            BasicTools.exitActivity(this)
        }


        if(!TemplateActivity.loginResponse?.data?.user?.image.isNullOrEmpty()){

          var urlLink= BasicTools.getUrlImg(this@CardActivityInsideSetting
              ,TemplateActivity.loginResponse?.data?.user?.image!!)

            BasicTools.loadImage(urlLink,img,object : DownloadListener {
                override fun completed(status: Boolean, bitmap: Bitmap) {
                   var anim = AnimationUtils.loadAnimation(this@CardActivityInsideSetting, android.R.anim.fade_in)
                    anim!!.setDuration(1000)
                    img.animation= anim
                }
            })}

    }


    override fun set_fragment_place() {

    }


}
