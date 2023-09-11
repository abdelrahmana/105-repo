package com.urcloset.smartangle.activity.forgotPassword


import android.os.Bundle
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.ActivtySelectWayBinding
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity

class SelectWayActivity : TemplateActivity() {
    var binding : ActivtySelectWayBinding?=null
    override fun set_layout() {
        binding = ActivtySelectWayBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

    }

    override fun init_events() {
        binding?.toolbar?.ivBack?.setOnClickListener {
            BasicTools.exitActivity(this@SelectWayActivity)
        }


        binding?.rootEmailWay?.setOnClickListener {

            BasicTools.openActivity(this@SelectWayActivity,EnterEmailActivityForgotPassword::class.java,false)
        }

        binding?.rootSmsWay?.setOnClickListener {

            BasicTools.openActivity(this@SelectWayActivity,EnterPhoneActivityForgotPassword::class.java,false)

        }

    }

    override fun set_fragment_place() {

    }


}
