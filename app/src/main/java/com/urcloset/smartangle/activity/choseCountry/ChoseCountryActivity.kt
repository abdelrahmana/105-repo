package com.urcloset.smartangle.activity.choseCountry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.cardview.widget.CardView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.databinding.ActivityChoseCountryBinding
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity

class ChoseCountryActivity : TemplateActivity() {
    lateinit var spinnerCountry : Spinner
    lateinit var spinnerCity : Spinner
    lateinit var cardFinish :CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var binding : ActivityChoseCountryBinding? =null
    override fun set_layout() {
        binding = ActivityChoseCountryBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

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
        spinnerCountry =binding!!.country
        spinnerCity  =binding!!.city
        cardFinish = binding!!.cardFinish
    }

    override fun init_events() {
        cardFinish.setOnClickListener {
            BasicTools.openActivity(this@ChoseCountryActivity, HomeActivity::class.java,true)


        }

    }

    override fun set_fragment_place() {
    }
}