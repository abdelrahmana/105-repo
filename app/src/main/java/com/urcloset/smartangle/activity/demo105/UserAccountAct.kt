package com.urcloset.smartangle.activity.demo105


import android.os.Bundle
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.ActivitySellerProfileBinding
import com.urcloset.smartangle.databinding.FragmentMyAccountBinding
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity

class UserAccountAct : TemplateActivity() {
    var binding : FragmentMyAccountBinding? =null
    override fun set_layout() {
        binding = FragmentMyAccountBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

    }

    override fun init_events() {

        binding?.ivTshirt?.setOnClickListener {

            BasicTools.openActivity(this@UserAccountAct,

                AddProductActivity::class.java, false)

        }

    }

    override fun set_fragment_place() {

    }


}
