package com.urcloset.smartangle.activity.locatonActivity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.*
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.choseCountry.ChoseCountryActivity
import com.urcloset.smartangle.activity.selectedCityActivity.SelectedCityActivity
import com.urcloset.smartangle.databinding.ActivityLocationBinding
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.Constants
import com.urcloset.smartangle.tools.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
import com.urcloset.smartangle.tools.Constants.PERMISSIONS_REQUEST_ENABLE_GPS
import com.urcloset.smartangle.tools.TemplateActivity

class LocationActivity : TemplateActivity() {
    lateinit var cardDenied : LinearLayout

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationPermissionGranted = false
    private var selectedLocation: Location?=null
    lateinit var shimmerAllow:ShimmerFrameLayout
    

    lateinit var btnAllow:CardView
    lateinit var ivBack:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var binding : ActivityLocationBinding? =null
    override fun set_layout() {
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

    }

    override fun init_activity(savedInstanceState: Bundle?) {
    }

    override fun init_views() {
        cardDenied = binding!!.cardDenied
        btnAllow=binding!!.cardAllow
        shimmerAllow = binding!!.shimmerAllow
        ivBack=binding!!.toolbar.ivBack

      //  BasicTools.openActivity(this@LocationActivity,SelectedCityActivity::class.java,false)
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this)

    }

    override fun init_events() {
        cardDenied.setOnClickListener {
            BasicTools.openActivity(this@LocationActivity,SelectedCityActivity::class.java,false)
        }
        
        btnAllow.setOnClickListener {

            if (checkMapServices()) {

                if (!mLocationPermissionGranted) {

                    Log.i("TEST_TEST","checkMapServices")
                    getLocationPermission() // get Permission
                } else {
                    Log.i("TEST_TEST","checkMapServices1235")
                    // //TODO OPEN THIS
                    getLastKnownLocation()
                    //BasicTools.openActivity(this@LocationActivity,SelectedCityActivity::class.java,false)
                }
            }
        }

        ivBack.setOnClickListener {
            BasicTools.exitActivity(this)
        }
    }

    override fun set_fragment_place() {
    }


    fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this@LocationActivity) //alert dialog object (yes,no)
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

    fun isServicesOK(): Boolean {
        Log.d("TEST_TEST", "isServicesOK: checking google services version")

        val available =
            GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this@LocationActivity)

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            Log.d("TEST_TEST", "isServicesOK: Google Play Services is working")
            return true
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Log.d("TEST_TEST", "isServicesOK: an error occured but we can fix it")
            val dialog = GoogleApiAvailability.getInstance()
                .getErrorDialog(this@LocationActivity, available, Constants.ERROR_DIALOG_REQUEST)
            dialog?.show()
        } else {
            this@LocationActivity.showToastMessage("You can't make map requests")
        }
        return false
    }

    fun isMapsEnabled(): Boolean {

        //get LOCATION_SERVICE object : service state
        val manager =this@LocationActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
                this@LocationActivity,
                android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) == PackageManager.PERMISSION_GRANTED*/
    fun getLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                this@LocationActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

        ) { //PERMISSION_GRANTED : the device accept it the fist run at  lolipop and above and accept in installation below lolipop
            mLocationPermissionGranted = true //GPS Permission is ok

            getLastKnownLocation()
            //TODO OPEN THIS
        //   BasicTools.openActivity(this@LocationActivity,SelectedCityActivity::class.java,false)






        }





        else {
            Log.i("TES_TEST","the permisttion")
            //runtime Permission
            ActivityCompat.requestPermissions(
                this@LocationActivity,
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
        if (requestCode.equals( PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)) {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED

                    ){
                    mLocationPermissionGranted = true
                    Log.i("TEST_TEST1400","$requestCode")
                    getLastKnownLocation()

                    //TODO OPEN THIS
                   // BasicTools.openActivity(this@LocationActivity,SelectedCityActivity::class.java,false)

                    // inflateUserListFragment();
                }

                else{
                    Log.i("TEST_TEST140","$requestCode")
                    if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Log.i("TEST_TEST","not 1")
                    if(grantResults[1] == PackageManager.PERMISSION_GRANTED)
                        Log.i("TEST_TEST","not 2")
                 /*   if(grantResults[2] == PackageManager.PERMISSION_GRANTED)
                        Log.i("TEST_TEST","not 3")*/

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
            PERMISSIONS_REQUEST_ENABLE_GPS -> {
                if (mLocationPermissionGranted) {
                    // inflateUserListFragment(); //run fragment
                    getLastKnownLocation()
                    //TODO OPEN THIS
                //    BasicTools.openActivity(this@LocationActivity,SelectedCityActivity::class.java,false)

                } else {
                    getLocationPermission()//get Permission
                }
            }
        }
    }

    fun getLastKnownLocation() {


        Log.i("TEST_TEST", "getLastKnownLocation: called.")
        if (ActivityCompat.checkSelfPermission(
                this@LocationActivity,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@LocationActivity,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        shimmerAllow.startShimmerAnimation()
        val locationRequest = LocationRequest.create().apply {
            interval = 2000
            fastestInterval = 2000

           numUpdates=1
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY}


            mFusedLocationClient!!.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        shimmerAllow.stopShimmerAnimation()

                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {

                            currenLocationVistor=location
                            Log.i("TEST_TEST",location.latitude.toString())
                            Log.i("TEST_TEST",location.longitude.toString())
                            BasicTools.openActivity(this@LocationActivity,SelectedCityActivity::class.java,true)

                        }

                    }
                },
                Looper.myLooper()!!
            )



    }

}