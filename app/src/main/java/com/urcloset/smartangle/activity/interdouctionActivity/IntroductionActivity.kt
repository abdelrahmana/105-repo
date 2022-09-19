package com.urcloset.smartangle.activity.interdouctionActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.urcloset.smartangle.CustomViewPager
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.SplashActivity
import com.urcloset.smartangle.adapter.IntroAdapter
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity

class IntroductionActivity : TemplateActivity() {
    lateinit var next:ImageView
    lateinit var pager:CustomViewPager
    var pos =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun set_layout() {
    setContentView(R.layout.activity_introduction)
    }

    override fun init_activity(savedInstanceState: Bundle?) {
        pager.adapter = IntroAdapter(this)
        pager.setPagingEnabled(false)


    }

    override fun init_views() {
        next = findViewById(R.id.next)
        pager = findViewById(R.id.view_pager)
        var item=BasicTools.isFirstLaunch(this@IntroductionActivity)
        Log.i("TEST_TEST","$item  is frist launch")
        if(!BasicTools.isFirstLaunch(this@IntroductionActivity))
            BasicTools.openActivity(this,SplashActivity::class.java,true)
    }

    override fun init_events() {
        next.setOnClickListener {

            if(pos<3) {
                BasicTools.setFirstLaunch(this,false)
                pager.setCurrentItem(pos)
                pos++
            }
            else if(pos==3){



                BasicTools.openActivity(this,SplashActivity::class.java,true)

            }


        }
    }

    override fun set_fragment_place() {
    }

}