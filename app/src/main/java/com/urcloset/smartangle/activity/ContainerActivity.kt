package com.urcloset.smartangle.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.ActivityContainerBinding
import com.urcloset.smartangle.fragment.directpay.DirectPayFragment
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContainerActivity : TemplateActivity() {
    lateinit var binding : ActivityContainerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        BasicTools.changeFragmentBack(
            this,
            DirectPayFragment(),
            "payment_direct_pay",
            null,
            R.id.root_fragment_home
        )
    }

    override fun set_layout() {
    }

    override fun init_activity(savedInstanceState: Bundle?) {
    }

    override fun init_views() {
    }

    override fun init_events() {
    }

    override fun set_fragment_place() {
    }
}