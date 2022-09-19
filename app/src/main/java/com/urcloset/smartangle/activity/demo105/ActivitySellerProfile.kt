package com.urcloset.smartangle.activity.demo105


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.urcloset.smartangle.R
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import kotlinx.android.synthetic.main.activity_seller_profile.*

class ActivitySellerProfile : TemplateActivity() {
    override fun set_layout() {
        setContentView(R.layout.activity_seller_profile)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

    }

    override fun init_events() {

        iv_next.setOnClickListener {
            BasicTools.openActivity(this@ActivitySellerProfile,

                UserAccountAct::class.java, false)

        }

    }

    override fun set_fragment_place() {

    }


}
