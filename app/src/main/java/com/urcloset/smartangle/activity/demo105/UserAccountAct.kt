package com.urcloset.smartangle.activity.demo105


import android.os.Bundle
import com.urcloset.smartangle.R
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import kotlinx.android.synthetic.main.fragment_my_account.*

class UserAccountAct : TemplateActivity() {
    override fun set_layout() {
        setContentView(R.layout.fragment_my_account)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

    }

    override fun init_events() {

        iv_tshirt.setOnClickListener {

            BasicTools.openActivity(this@UserAccountAct,

                AddProductActivity::class.java, false)

        }

    }

    override fun set_fragment_place() {

    }


}
