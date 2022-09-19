package com.urcloset.smartangle.activity.selectedCityActivity


import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.cardview.widget.CardView
import com.facebook.shimmer.ShimmerFrameLayout
import com.hbb20.CountryCodePicker
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.Email
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.adapter.project105.CountrySpinnerAdapter
import com.urcloset.smartangle.adapter.project105.CountryWithCitySpinnerAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.CountryModel
import com.urcloset.smartangle.model.project_105.CountryWithCity
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SelectedCityActivity : TemplateActivity() {

    lateinit var spinnerCountry: Spinner
    lateinit var spinnerCountryWithCity: Spinner


    lateinit var shimmerCountry: ShimmerFrameLayout
    lateinit var shimmerCountryWithCity: ShimmerFrameLayout

    lateinit var adapterCountry: CountrySpinnerAdapter
    lateinit var adapterCountryWithCity: CountryWithCitySpinnerAdapter


    
    lateinit var ivBack:ImageView

    lateinit var countryCode: CountryCodePicker
   var disposable= CompositeDisposable()


    lateinit var btnNext:CardView
    override fun set_layout() {
        setContentView(R.layout.activity_select_city)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

        TemplateActivity.selectedCityVisitor=""
        TemplateActivity.selectedCountryVisitor=""
        spinnerCountry=findViewById(R.id.spinner_country)
        spinnerCountryWithCity=findViewById(R.id.spinner_city)
        btnNext=findViewById(R.id.card_next)

        shimmerCountry=findViewById(R.id.shimmer_country)
        shimmerCountryWithCity=findViewById(R.id.shimmer_city)

        ivBack=findViewById(R.id.iv_back)


    }

    override fun init_events() {


        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>=0){


                    var item: CountryModel.Data= adapterCountry.getItem(position)!!
                    getCountryWithCity(item.id.toString())
                    TemplateActivity.selectedCountryVisitor=item.id.toString()
                    TemplateActivity.selectedCountryNameArVisitor = item.nameAr.toString()
                    TemplateActivity.selectedCountryNameVisitor = item.name.toString()



                }

            }
        }


        spinnerCountryWithCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position>=0){


                    var item: CountryWithCity.Data.Citty= adapterCountryWithCity.getItem(position)!!
                    //getCountryWithCity(item.id.toString())
                    if(item!=null &&item.id!=null)
                    TemplateActivity.selectedCityVisitor=item.id.toString()
                    TemplateActivity.selectedCityNameArVisitor = item.nameAr.toString()
                    TemplateActivity.selectedCityNameVisitor = item.name.toString()
                }

            }
        }
        
        ivBack.setOnClickListener { 
            
            BasicTools.exitActivity(this@SelectedCityActivity)
        }
        
        
        btnNext.setOnClickListener { 


            BasicTools.clearAllActivity(this@SelectedCityActivity,HomeActivity::class.java)

        }

        getCountry()

    }

    override fun set_fragment_place() {

    }



    fun getCountry(){
        if (BasicTools.isConnected(this@SelectedCityActivity)) {



            BasicTools.showShimmer(spinnerCountry,shimmerCountry,true)

            var map = HashMap<String, String>()
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(this@SelectedCityActivity).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.getCountry()
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CountryModel>(this@SelectedCityActivity) {
                        override fun onSuccess(result: CountryModel) {


                            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)


                            adapterCountry= CountrySpinnerAdapter(this@SelectedCityActivity,
                                result.data!!)

                            spinnerCountry.adapter=adapterCountry


                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            BasicTools.showShimmer(spinnerCountry,shimmerCountry,false)
            showToastMessage(R.string.no_connection)}
    }


    fun getCountryWithCity(id:String){
        if (BasicTools.isConnected(this@SelectedCityActivity)) {



            BasicTools.showShimmer(spinnerCountryWithCity,shimmerCountryWithCity,true)

            var map = HashMap<String, String>()
            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(this@SelectedCityActivity).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.getCountryWithCity(id)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CountryWithCity>(this@SelectedCityActivity) {
                        override fun onSuccess(result: CountryWithCity) {


                            BasicTools.showShimmer(spinnerCountryWithCity,shimmerCountryWithCity,false)


                            adapterCountryWithCity= CountryWithCitySpinnerAdapter(this@SelectedCityActivity,
                                result.data?.citties!!)

                            spinnerCountryWithCity.adapter=adapterCountryWithCity


                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(spinnerCountryWithCity,shimmerCountryWithCity,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {

            showToastMessage(R.string.no_connection)}
    }



}
