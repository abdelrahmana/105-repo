package com.urcloset.smartangle.activity.searchActivity


import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.zawadz88.materialpopupmenu.popupMenu
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.selectedCityActivity.SelectedCityActivity
import com.urcloset.smartangle.adapter.project105.ProviderSearchAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.ProivderSeachModel
import com.urcloset.smartangle.model.project_105.SearchProductV2Model
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_search_provider.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*

class SearchActivityProvider : TemplateActivity(),ISearchProviderActivity {

    lateinit var cardRate2:CardView
    lateinit var cardRate3:CardView
    lateinit var cardRate4:CardView
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationPermissionGranted = false
    private var selectedLocation: Location?=null
    lateinit var cardTextRate2:TextView
    lateinit var cardTextRate3:TextView
    lateinit var cardTextRate4:TextView
    lateinit var cardTextAllUser:TextView
    lateinit var cardTextCurrentLocation:TextView
    lateinit var ivFilter:ImageView
    var filterType= Constants.FILTER_TYPE_ITEM
    lateinit var  popupMenu: PopupMenu
    lateinit var seekbar:SeekBar

    lateinit var seekbarText:TextView

    lateinit var rv:RecyclerView
    lateinit var adapter:ProviderSearchAdapter

    lateinit var layoutManagerSearch: GridLayoutManager
    var rateValue="2"
    var ditanceValue="-1"
    var disposableSearch: CompositeDisposable = CompositeDisposable()
    lateinit var cardAllUser:CardView
    lateinit var cardCurrentLocation:CardView

    lateinit var btnSearch:CardView
    lateinit var shimmerSearch: ShimmerFrameLayout
    lateinit var editSearch: EditText

    lateinit var ivBack:ImageView

    var requestBegin=false
    var page=0
    var lastPage=-1
    lateinit var prgs:ProgressBar

    var serachByLocation="0"

    lateinit var rootEmpty:LinearLayout
    override fun set_layout() {
        setContentView(R.layout.activity_search_provider)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this)
        adapter=ProviderSearchAdapter(this@SearchActivityProvider, ArrayList(),this)
        cardRate2=card_rate2
        cardRate3=card_rate3
        cardRate4=card_rate4
        editSearch=findViewById(R.id.edit_search_product)
        rv=rv_search_history
        seekbar=seek_bar_distance
        seekbarText=tv_seekbar
        ivFilter=findViewById(R.id.iv_filter)
        cardAllUser=card_all_users
        cardCurrentLocation=card_you_area

        prgs=findViewById(R.id.prgs_search_provider)

        rootEmpty=findViewById(R.id.root_empty_search)
        ivBack=iv_back
        cardTextRate2=tv_rate2
        cardTextRate3=tv_rate3
        cardTextRate4=tv_rate4

        layoutManagerSearch= GridLayoutManager(this,2, LinearLayoutManager.HORIZONTAL,false)
        cardTextCurrentLocation=tv_current_location
        cardTextAllUser=tv_all_user
        btnSearch=findViewById(R.id.btn_search)
        shimmerSearch=findViewById(R.id.shimmer_search)

        rv.layoutManager=layoutManagerSearch
        rv.adapter=adapter






    }


    fun selectedItem(state: Boolean,cardView: CardView,textView: TextView){
        if(state){
            cardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.login_btn))
            textView.setTextColor(ContextCompat.getColor(this,R.color.white))
            cardView.radius=BasicTools.dpToPxFloat(10,this)
        }else{

            cardView.setCardBackgroundColor(ContextCompat.getColor(this,R.color.background_edit_text))
            textView.setTextColor(ContextCompat.getColor(this,R.color.login_btn))
            cardView.radius=BasicTools.dpToPxFloat(10,this)
        }
    }

    override fun init_events() {

        selectedItem(true,cardRate2,cardTextRate2)
        cardRate2.setOnClickListener {
            if(rateValue.isEmpty()|| rateValue!="2"){
                selectedItem(true,cardRate2,cardTextRate2)
                selectedItem(false,cardRate3,cardTextRate3)
                selectedItem(false,cardRate4,cardTextRate4)
                rateValue="2"
            }

            else {
                rateValue=""
                selectedItem(false,cardRate2,cardTextRate2)
                selectedItem(false,cardRate3,cardTextRate3)
                selectedItem(false,cardRate4,cardTextRate4)
            }
        }


        cardRate3.setOnClickListener {
            if(rateValue.isEmpty()|| rateValue!="3"){
                selectedItem(false,cardRate2,cardTextRate2)
                selectedItem(true,cardRate3,cardTextRate3)
                selectedItem(false,cardRate4,cardTextRate4)
                rateValue="3"
            }

            else {
                rateValue=""
                selectedItem(false,cardRate2,cardTextRate2)
                selectedItem(false,cardRate3,cardTextRate3)
                selectedItem(false,cardRate4,cardTextRate4)
            }
        }


        cardRate4.setOnClickListener {
            if(rateValue.isEmpty()|| rateValue!="4"){
                selectedItem(false,cardRate2,cardTextRate2)
                selectedItem(false,cardRate3,cardTextRate3)
                selectedItem(true,cardRate4,cardTextRate4)
                rateValue="4"
            }

            else {
                rateValue=""
                selectedItem(false,cardRate2,cardTextRate2)
                selectedItem(false,cardRate3,cardTextRate3)
                selectedItem(false,cardRate4,cardTextRate4)
            }
        }





        cardCurrentLocation.setOnClickListener {
            selectedItem(true,cardCurrentLocation,cardTextCurrentLocation)
            selectedItem(false,cardAllUser,cardTextAllUser)
           serachByLocation="1"


            if (checkMapServices()) {

                if (!mLocationPermissionGranted) {

                    Log.i("TEST_TEST","checkMapServices")
                    getLocationPermission() // get Permission
                } else {
                    Log.i("TEST_TEST","checkMapServices1235")
                    // //TODO OPEN THIS
                    // BasicTools.openActivity(this@LocationActivity,SelectedCityActivity::class.java,false)
                }
            }
        }

        cardAllUser.setOnClickListener {
            selectedItem(true,cardAllUser,cardTextAllUser)
            selectedItem(false,cardCurrentLocation,cardTextCurrentLocation)
            serachByLocation="0"
            selectedLocation=null

        }


        seekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {

                ditanceValue=progress.toString()
                seekbarText.setText("$progress  KM")

            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

        ivBack.setOnClickListener {
            BasicTools.exitActivity(this@SearchActivityProvider)
        }

        btnSearch.setOnClickListener {

             page=0
             lastPage=-1
            getSearch()
        }


        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var direction=1
                if(BasicTools.isDeviceLanEn())
                    direction=1
                else direction=-1
                if (!recyclerView.canScrollHorizontally(direction) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(!requestBegin &&lastPage!=page)
                        getSearch()
                }
            }
        })



        ivFilter.setOnClickListener {
            // popupMenu.show()

            val popupMenu = popupMenu {
                style = R.style.Widget_MPM_Menu_Dark_CustomBackground
                section {

                    item {
                        labelRes = R.string.product
                        labelColor = ContextCompat.getColor(this@SearchActivityProvider, R.color.black)
                        iconDrawable = ContextCompat.getDrawable(this@SearchActivityProvider, R.drawable.selected_icon)
                        callback = { //optional
                            BasicTools.openActivity(this@SearchActivityProvider,SearchActivity::class.java,true)
                        }
                        iconColor = resources.getColor(R.color.white)

                    }
                    item {
                        labelRes = R.string.porvider
                        labelColor = ContextCompat.getColor(this@SearchActivityProvider, R.color.black)
                        iconDrawable = ContextCompat.getDrawable(this@SearchActivityProvider, R.drawable.selected_icon)

                        callback = { //optional


                        }
                        iconColor = resources.getColor(R.color.colorPrimary)

                    }

                }
            }


            popupMenu.show(this@SearchActivityProvider, ivFilter)
        }

    }

    override fun set_fragment_place() {

    }

    fun getSearch(){
        if (BasicTools.isConnected(this@SearchActivityProvider)) {



            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@SearchActivityProvider).toString())
                ?.create(AppApi::class.java)


            var map=HashMap<String,String>()
            map.put("page",(++page).toString())

            if(editSearch.text.isNotEmpty())
                map.put("search_text",editSearch.text.trim().toString())

            if(!ditanceValue.equals("-1"))
                map.put("distance",ditanceValue)

            if(serachByLocation.equals("1") && selectedLocation!=null){
                map.put("lat",selectedLocation?.latitude.toString())
                map.put("long",selectedLocation?.longitude.toString())
                map.put("serach_by_location",serachByLocation)
            }
            else
            map.put("serach_by_location","0")
            map.put("rate",rateValue)




            if(page==1){
                BasicTools.showShimmer(btnSearch,shimmerSearch,true)
                prgs.visibility=View.GONE
            }
            else{
                prgs.visibility=View.VISIBLE
            }
            val observable= shopApi!!.searchProvider(map)
            disposableSearch.clear()
            disposableSearch.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ProivderSeachModel>(this@SearchActivityProvider) {
                        override fun onSuccess(result: ProivderSeachModel) {
                            BasicTools.showShimmer(btnSearch,shimmerSearch,false)
                            page=result.data!!.currentPage!!
                            lastPage=result.data!!.lastPage!!
                            prgs.visibility= View.GONE
                            requestBegin=false

                            if(result.status!!) {


                                if(page==1)
                                adapter.change_data(result.data?.data!!)

                                else adapter.addRefersh(result.data?.data!!)


                            }


                        }
                        override fun onFailed(status: Int) {
                            requestBegin=false
                            prgs.visibility=View.GONE
                            BasicTools.showShimmer(btnSearch,shimmerSearch,false)
                            showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            showToastMessage(R.string.no_connection)}
    }

    override fun checkIfAdapterEmpty(item: Boolean) {
        if(item){

            rootEmpty.visibility=View.VISIBLE

        }else{
            rootEmpty.visibility=View.GONE
        }
    }



    fun isServicesOK(): Boolean {
        Log.d("TEST_TEST", "isServicesOK: checking google services version")

        val available =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@SearchActivityProvider)

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("TEST_TEST", "isServicesOK: Google Play Services is working")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d("TEST_TEST", "isServicesOK: an error occured but we can fix it")
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this@SearchActivityProvider, available, Constants.ERROR_DIALOG_REQUEST)
            dialog?.show()
        } else {
            this@SearchActivityProvider.showToastMessage("You can't make map requests")
        }
        return false
    }
    fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this@SearchActivityProvider) //alert dialog object (yes,no)
        builder.setMessage(resources.getString(R.string.gps_hint))
            .setCancelable(true)
            .setPositiveButton(resources.getString(R.string.yes), DialogInterface.OnClickListener { dialog, id ->
                // K Button
                val enableGpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(enableGpsIntent, Constants.PERMISSIONS_REQUEST_ENABLE_GPS)
            }).setNegativeButton(resources.getString(R.string.no), DialogInterface.OnClickListener{ dialog, id ->
                dialog.dismiss()
            })
        val alert = builder.create()
        alert.show()

    }
    fun isMapsEnabled(): Boolean {

        //get LOCATION_SERVICE object : service state
        val manager =this@SearchActivityProvider.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        //if the GPS service not enable show the alert Dialog
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
            return false
        }
        return true
    }




    fun checkMapServices(): Boolean {
        if (isServicesOK()) {  //
            if (isMapsEnabled()) {
                return true
            }
        }
        return false

    }



    /*    ContextCompat.checkSelfPermission(
                this@SearchActivityProvider,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED*/
    fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                this@SearchActivityProvider,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        ) { //PERMISSION_GRANTED : the device accept it the fist run at  lolipop and above and accept in installation below lolipop
            mLocationPermissionGranted = true //GPS Permission is ok

            getLastKnownLocation()
            //TODO OPEN THIS
            //   BasicTools.openActivity(this@SearchActivityProvider,SelectedCityActivity::class.java,false)






        }





        else {
            Log.i("TES_TEST","the permisttion")
            //runtime Permission
            ActivityCompat.requestPermissions(
                this@SearchActivityProvider,
                arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                ),
                Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        mLocationPermissionGranted = false

        Log.i("TEST_TEST14","$requestCode")
        if (requestCode.equals(Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)) {

            // If request is cancelled, the result arrays are empty.
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED

            ){
                mLocationPermissionGranted = true
                Log.i("TEST_TEST1400","$requestCode")
                getLastKnownLocation()

                //TODO OPEN THIS
                // BasicTools.openActivity(this@SearchActivityProvider,SelectedCityActivity::class.java,false)

                // inflateUserListFragment();
            }

            else{
                Log.i("TEST_TEST140","$requestCode")
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Log.i("TEST_TEST","not 1")
                if(grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    Log.i("TEST_TEST","not 2")
                if(grantResults[2] == PackageManager.PERMISSION_GRANTED)
                    Log.i("TEST_TEST","not 3")

            }
        }else {
            Log.i("TEST_TEST","not 1258")

        }

    }

    private fun setLocationListner() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy

// getting location every 5 secs, for something very accurate
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {

                    }
                    // Things don't end here
                    // You may also update the location on your web app
                }
            },
            Looper.myLooper()!!
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("TEST_TEST", "onActivityResult: called.")
        when (requestCode) {
            Constants.PERMISSIONS_REQUEST_ENABLE_GPS -> {
                if (mLocationPermissionGranted) {
                    // inflateUserListFragment(); //run fragment
                    getLastKnownLocation()
                    //TODO OPEN THIS
                    //    BasicTools.openActivity(this@SearchActivityProvider,SelectedCityActivity::class.java,false)

                } else {
                    getLocationPermission()//get Permission
                }
            }
        }
    }

    fun getLastKnownLocation() {


        Log.i("TEST_TEST", "getLastKnownLocation: called.")
        if (ActivityCompat.checkSelfPermission(
                this@SearchActivityProvider,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@SearchActivityProvider,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }

        val locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 2000


            priority = LocationRequest.PRIORITY_HIGH_ACCURACY}


        mFusedLocationClient!!.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {

                        selectedLocation=location
                        Log.i("TEST_TEST",location.latitude.toString())
                        Log.i("TEST_TEST",location.longitude.toString())


                    }

                }
            },
            Looper.myLooper()!!
        )



    }


}
