package com.urcloset.smartangle.activity.orderSentActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.publishStatusActivity.PublicationStatus
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity

class OrderSentActivity : TemplateActivity() {
    lateinit var  card_finish:CardView
    lateinit var ivBack:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun set_layout() {
        setContentView(R.layout.activity_order_sent)
    }

    override fun init_activity(savedInstanceState: Bundle?) {
    }

    override fun init_views() {
        card_finish = findViewById(R.id.card_finish)
        ivBack = findViewById(R.id.iv_back)
    }

    override fun init_events() {
        card_finish.setOnClickListener {
            BasicTools.openActivity(this,PublicationStatus::class.java,true)
        }
        ivBack.setOnClickListener {
            BasicTools.exitActivity(this)
        }
    }

    override fun set_fragment_place() {
    }
}