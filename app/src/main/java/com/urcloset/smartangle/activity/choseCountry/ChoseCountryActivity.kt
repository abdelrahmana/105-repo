package com.urcloset.smartangle.activity.choseCountry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.cardview.widget.CardView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import kotlinx.android.synthetic.main.activity_chose_country.*

class ChoseCountryActivity : TemplateActivity() {
    lateinit var spinnerCountry : Spinner
    lateinit var spinnerCity : Spinner
    lateinit var cardFinish :CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun set_layout() {
        setContentView(R.layout.activity_chose_country)

    }

    override fun init_activity(savedInstanceState: Bundle?) {
        val  countries = ArrayList<String>()
        countries.add("Morocco")
        countries.add("UAE")
        val  cities = ArrayList<String>()
        cities.add("Marrakech")
        val  locations = ArrayList<String>()
        locations.add("Current Location")



        val countryAdapter: ArrayAdapter<String?>? = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            countries.toList()
        )
        val cityAdapter: ArrayAdapter<String?>? = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            cities.toList()
        )
        val locationAdapter: ArrayAdapter<String?>? = ArrayAdapter(
            this, android.R.layout.simple_spinner_dropdown_item,
            locations.toList()
        )

        spinnerCountry.adapter = countryAdapter
        spinnerCity.adapter = cityAdapter
        spinnerCity.adapter = locationAdapter
    }

    override fun init_views() {
        spinnerCountry =country
        spinnerCity  = city
        cardFinish = card_finish
    }

    override fun init_events() {
        cardFinish.setOnClickListener {
            BasicTools.openActivity(this@ChoseCountryActivity, HomeActivity::class.java,true)


        }

    }

    override fun set_fragment_place() {
    }
}