package com.urcloset.smartangle.fragment.HomeFragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.activity.notificationActivity.NotificationActivity
import com.urcloset.smartangle.activity.searchActivity.SearchActivity
import com.urcloset.smartangle.adapter.UserGridAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.fragment.setting_fragment.SettingFragment
import com.urcloset.smartangle.fragment.myselleraccount.MySellerAccount
import com.urcloset.smartangle.fragment.seeAllUserFragment.SeeAllUserFragment
import com.urcloset.smartangle.model.NearbyUsersModel
import com.urcloset.smartangle.model.NotificationModel
import com.urcloset.smartangle.model.UserProfileModel
import com.urcloset.smartangle.model.UsersModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import com.urcloset.smartangle.tools.TemplateFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home1.*
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : TemplateFragment() {
    var disposable= CompositeDisposable()
    lateinit var areaViewPager:ViewPager
    lateinit var shimmerWait :ShimmerFrameLayout
    lateinit var shimmerWaitUsers :ShimmerFrameLayout
    lateinit var etSearch:EditText
    lateinit var tvEmpty:TextView
    var usersSearchResult =  ArrayList<NearbyUsersModel.Data.NearbyUsers.User>()
    var allNearbyUsers =  ArrayList<NearbyUsersModel.Data.NearbyUsers.User>()
    lateinit var nearbyUsersModel:NearbyUsersModel
    lateinit var firstResult:NearbyUsersModel

    var page = 1
    var lastPage:Int?=null
    lateinit var userGridAdapter:UserGridAdapter
    companion object{
        var allUsers =  ArrayList<NearbyUsersModel.Data.NearbyUsers.User>()

    }

    override fun onResume() {
        if(HomeActivity.bottomNavigation?.currentItem!=1){
            HomeActivity.doNothing =  true
            HomeActivity.bottomNavigation?.currentItem=1
            HomeActivity.doNothing =  false

        }
        super.onResume()
    }

    override fun onAttach(context: Context) {


        super.onAttach(context)
    }



    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home1, container, false)
        areaViewPager = view.findViewById(R.id.area_view_pager)
        shimmerWait = view.findViewById(R.id.shimmer_wait)
        shimmerWaitUsers = view.findViewById(R.id.shimmer_users)
        etSearch = view.findViewById(R.id.et_search)
        tvEmpty = view.findViewById(R.id.tv_empty)
        if(BasicTools.isConnected(parent!!)) {
            shimmerWait.visible()
            shimmerWaitUsers.visible()
            areaViewPager.visibility = View.GONE


            val shopApi = ApiClient.getClient(
                BasicTools.getProtocol(activity!!.applicationContext).toString(), "en"
            )?.create(
                AppApi::class.java
            )
            val map = HashMap<String, String>()
            map.put("per_page","36")

            if(TemplateActivity.loginResponse?.data?.accessToken!=null){
                if(!TemplateActivity.loginResponse?.data?.user?.lat.isNullOrEmpty())
                map.put("lat", TemplateActivity.loginResponse?.data?.user?.lat!!)
                if(!TemplateActivity.loginResponse?.data?.user?.long.isNullOrEmpty())
                    map.put("long", TemplateActivity.loginResponse?.data?.user?.long!!)
                if(!TemplateActivity.loginResponse?.data?.user?.cityId.isNullOrEmpty())
                    map.put("city_id", TemplateActivity.loginResponse?.data?.user?.cityId!!)
                if(!TemplateActivity.loginResponse?.data?.user?.countryId.isNullOrEmpty())
                    map.put("country_id", TemplateActivity.loginResponse?.data?.user?.cityId!!)

            }
            else{
                if(TemplateActivity.currenLocationVistor!=null) {
                    map.put("lat", TemplateActivity.currenLocationVistor!!.latitude.toString())
                    map.put("long", TemplateActivity.currenLocationVistor!!.longitude.toString())
                }
                if(!TemplateActivity.selectedCityVisitor.isNullOrEmpty())
                    map.put("city_id", TemplateActivity.selectedCityVisitor)
                if(!TemplateActivity.selectedCountryVisitor.isNullOrEmpty())
                    map.put("country_id", TemplateActivity.selectedCountryVisitor)

            }
            val observable = shopApi!!.getUsers(map)
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<NearbyUsersModel>(context!!) {
                        override fun onSuccess(result: NearbyUsersModel) {
                            shimmerWaitUsers.hide()
                            shimmerWait.hide()
                            shimmerWait.visibility = View.GONE
                            shimmerWaitUsers.visibility = View.GONE
                            areaViewPager.visibility = View.VISIBLE
                            if (result.status!!) {
                                firstResult = result.copy(
                                    data = result.data?.copy(nearbyUsers =result.data?.nearbyUsers!!.copy() ))
                                allUsers = result.data?.nearbyUsers?.data!!
                                nearbyUsersModel = result
                                lastPage = result.data?.nearbyUsers?.lastPage
                                if(!TemplateActivity.loginResponse?.data?.accessToken.isNullOrEmpty()) {
                                    allNearbyUsers = result.data?.nearbyUsers?.data!!.filter {
                                        (TemplateActivity.loginResponse?.data?.user?.id != it.id)
                                    } as ArrayList<NearbyUsersModel.Data.NearbyUsers.User>




                                }
                                else {
                                    allNearbyUsers = result.data?.nearbyUsers?.data!!

                                }

                                userGridAdapter = UserGridAdapter(context, result)
                                areaViewPager.adapter = userGridAdapter

                                if ( allNearbyUsers.size <= 0) {
                                    view.findViewById<LinearLayout>(R.id.ly_empty).visibility =
                                        View.VISIBLE
                                    view.findViewById<TextView>(R.id.tv_area_title).visibility =
                                        View.GONE

                                    areaViewPager.visibility = View.GONE

                                } else {
                                    if( allNearbyUsers.size==0)
                                        tvEmpty.visibility = View.VISIBLE
                                    else tvEmpty.visibility = View.GONE
                                    view.findViewById<LinearLayout>(R.id.ly_empty).visibility =
                                        View.GONE
                                    view.findViewById<TextView>(R.id.tv_area_title).visibility =
                                        View.VISIBLE

                                    areaViewPager.visibility = View.VISIBLE


                                }

                            }


                        }

                        override fun onFailed(status: Int) {
                            shimmerWait.hide()
                            shimmerWait.visibility = View.GONE
                            areaViewPager.visibility = View.VISIBLE
                            shimmerWait.visibility = View.GONE
                            shimmerWaitUsers.visibility = View.GONE


                        }
                    })

            )
        }
        else{
            Toast.makeText(parent, R.string.no_connection, Toast.LENGTH_SHORT).show()
        }

        return view
    }

    override fun init_views() {




    }

    override fun init_events() {

        etSearch.addTextChangedListener (
            object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(p0.toString().isNotEmpty()) {

                        usersSearchResult = allUsers.filter {
                            it.name!!.contains(p0.toString(), true)
                        } as ArrayList<NearbyUsersModel.Data.NearbyUsers.User>
                        nearbyUsersModel.data?.nearbyUsers?.data = usersSearchResult
                        nearbyUsersModel.data?.nearbyUsers?.total = usersSearchResult.size
                        nearbyUsersModel.data?.nearbyUsers?.lastPage = Math.ceil(usersSearchResult.size/26.0).toInt()
                        userGridAdapter = UserGridAdapter(context, nearbyUsersModel)
                        areaViewPager.adapter = userGridAdapter
                        userGridAdapter.update()
                    }
                    else {
                        areaViewPager.adapter = UserGridAdapter(context, firstResult.copy())
                        userGridAdapter.update()

                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            }


        )
    }

    override fun init_fragment(savedInstanceState: Bundle?) {

    }

    override fun on_back_pressed(): Boolean {
        return true
    }



}