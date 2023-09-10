package com.urcloset.smartangle.activity.demo105


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.ActivitySellerProfileBinding
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity

class ActivitySellerProfile : TemplateActivity() {
    var binding : ActivitySellerProfileBinding? =null
    override fun set_layout() {
        binding = ActivitySellerProfileBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

    }

    override fun init_events() {

        binding?.ivNext?.setOnClickListener {
            BasicTools.openActivity(this@ActivitySellerProfile,

                UserAccountAct::class.java, false)

        }

    }

    override fun set_fragment_place() {

    }


}
