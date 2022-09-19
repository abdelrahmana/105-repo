package com.urcloset.smartangle.activity.forgotPassword


import android.os.Bundle
import com.urcloset.smartangle.R
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import kotlinx.android.synthetic.main.activty_select_way.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*

class SelectWayActivity : TemplateActivity() {
    override fun set_layout() {
        setContentView(R.layout.activty_select_way)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

    }

    override fun init_events() {
        iv_back.setOnClickListener {
            BasicTools.exitActivity(this@SelectWayActivity)
        }


        root_email_way.setOnClickListener {

            BasicTools.openActivity(this@SelectWayActivity,EnterEmailActivityForgotPassword::class.java,false)
        }

        root_sms_way.setOnClickListener {

            BasicTools.openActivity(this@SelectWayActivity,EnterPhoneActivityForgotPassword::class.java,false)

        }

    }

    override fun set_fragment_place() {

    }


}
