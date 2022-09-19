package com.urcloset.smartangle.activity.cardActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.verification_code.EnterCodeActivity
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity

class CardActivity : TemplateActivity() {

    lateinit var btnNext:CardView
    lateinit var name:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun set_layout() {
        setContentView(R.layout.activity_card)
    }

    override fun init_activity(savedInstanceState: Bundle?) {
    }

    override fun init_views() {

        name=findViewById(R.id.tv_name)
        btnNext=findViewById(R.id.card_contineu)
    }

    override fun init_events() {


        name.setText(TemplateActivity.cardResult?.data?.name)
        btnNext.setOnClickListener {
            BasicTools.openActivity(this@CardActivity,
                EnterCodeActivity::class.java,false)
        }
    }

    override fun set_fragment_place() {
    }
}


