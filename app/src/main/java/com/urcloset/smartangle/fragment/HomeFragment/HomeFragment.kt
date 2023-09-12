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
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.UserGridAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.fragment.HomeFragment.adaptor.UsersAdaptorList
import com.urcloset.smartangle.model.NearbyUsersModel
import com.urcloset.smartangle.tools.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList
class HomeFragment : TemplateFragment(),NestedScrollPaginationView.OnMyScrollChangeListener {
    var disposable= CompositeDisposable()
    lateinit var areaViewPager:RecyclerView
    lateinit var shimmerWait :ShimmerFrameLayout
    lateinit var shimmerWaitUsers :ShimmerFrameLayout
    lateinit var etSearch:EditText
    lateinit var tvEmpty:TextView
    var usersSearchResult =  ArrayList<NearbyUsersModel.Data.User>()
    var allNearbyUsers =  ArrayList<NearbyUsersModel.Data.User>()
    lateinit var nearbyUsersModel:NearbyUsersModel
    lateinit var firstResult:NearbyUsersModel
    lateinit var  adapter : UsersAdaptorList

    var page = 1
    var lastPage:Int?=null
    lateinit var userGridAdapter:UserGridAdapter
    companion object{
        var allUsers =  ArrayList<NearbyUsersModel.Data.User>()

    }

    override fun onResume() {
       /* if(HomeActivity.bottomNavigation?.currentItem!=1){
            HomeActivity.doNothing =  true
            HomeActivity.bottomNavigation?.currentItem=1
            HomeActivity.doNothing =  false

        }*/
        super.onResume()
    }

    override fun onAttach(context: Context) {


        super.onAttach(context)
    }
     var shopApi : AppApi ? =null



    lateinit var views : View
    lateinit var  nestedScrollPaginationView : NestedScrollPaginationView
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         views = inflater.inflate(R.layout.fragment_home1, container, false)
        areaViewPager = views.findViewById(R.id.area_view_pager)
        val swipeRefreshLayout : SwipeRefreshLayout  = views.findViewById(R.id.swipe)
         nestedScrollPaginationView = views.findViewById(R.id.nestedScrollPagination)
        nestedScrollPaginationView.myScrollChangeListener = this

        shimmerWait = views.findViewById(R.id.shimmer_wait)
        shimmerWaitUsers = views.findViewById(R.id.shimmer_users)
        etSearch = views.findViewById(R.id.et_search)
        tvEmpty = views.findViewById(R.id.tv_empty)
         adapter = UsersAdaptorList(null,allNearbyUsers)
        BasicTools.setRecycleView(areaViewPager,adapter,
            null,requireContext(),GridModel(3,10),false)
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            nestedScrollPaginationView.resetPageCounter()
            getUsersCall()
            swipeRefreshLayout.isRefreshing = false
        }
        if (allNearbyUsers.isEmpty())
            getUsersCall()

        viewModelHome.setPreviousNavBottom(R.id.users)
        return views
    }

    private fun getUsersCall() {
        if(BasicTools.isConnected(parent!!)) {
            shimmerWait.visible()
            shimmerWaitUsers.visible()
            areaViewPager.visibility = View.GONE


            shopApi = ApiClient.getClient(
                BasicTools.getProtocol(activity!!.applicationContext).toString(), "en"
            )?.create(
                AppApi::class.java
            )
            val map = HashMap<String, String>()
            map.put("per_page","36")
            map.put("page",page.toString())
/*
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

            }*/
            setCall(map)
        }
        else{
            Toast.makeText(parent, R.string.no_connection, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setCall(map: HashMap<String, String>) {
        views.findViewById<LinearLayout>(R.id.ly_empty).visibility =
            View.GONE
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
                                data = result.data?.copy(data =result.data?.data!! ))
                            allUsers = result.data?.data!!
                            nearbyUsersModel = result
                            lastPage = result.data?.lastPage
                            var newNearbyList = ArrayList<NearbyUsersModel.Data.User>()
                            if(!TemplateActivity.loginResponse?.data?.accessToken.isNullOrEmpty()) {
                                newNearbyList = result.data?.data!!.filter {
                                    (TemplateActivity.loginResponse?.data?.user?.id != it.id)
                                } as ArrayList<NearbyUsersModel.Data.User>




                            }
                            else {
                                newNearbyList = result.data?.data!!

                            }
                            adapter.updateList(newNearbyList)

                         //   userGridAdapter = UserGridAdapter(context, result)
                         //   areaViewPager.adapter = userGridAdapter

                            if ( allNearbyUsers.size <= 0) {
                                views?.findViewById<LinearLayout>(R.id.ly_empty)?.visibility =
                                    View.VISIBLE
                             //   views?.findViewById<TextView>(R.id.tv_area_title).visibility =
                                //    View.GONE

                                areaViewPager.visibility = View.GONE

                            } else {
                                if( allNearbyUsers.size==0)
                                    tvEmpty.visibility = View.VISIBLE
                                else tvEmpty.visibility = View.GONE
                                views.findViewById<LinearLayout>(R.id.ly_empty).visibility =
                                    View.GONE
                                views.findViewById<TextView>(R.id.tv_area_title).visibility =
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

    override fun init_views() {




    }

    override fun init_events() {

        etSearch.addTextChangedListener (
            object :TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(p0.toString().isNotEmpty()) {

                     /*   usersSearchResult = allUsers.filter {
                            it.name!!.contains(p0.toString(), true)
                        } as ArrayList<NearbyUsersModel.Data.NearbyUsers.User>
                        nearbyUsersModel.data?.data?.data = usersSearchResult
                        nearbyUsersModel.data?.data?.total = usersSearchResult.size
                        nearbyUsersModel.data?.data?.lastPage = Math.ceil(usersSearchResult.size/26.0).toInt()
                     //   userGridAdapter = UserGridAdapter(context, nearbyUsersModel)
                        adapter = UsersAdaptorList(null,usersSearchResult)
                        areaViewPager.adapter = adapter//userGridAdapter
                        adapter.notifyDataSetChanged()

                      */
                       // userGridAdapter.update()
                        shimmerWait.visible()
                        shimmerWaitUsers.visible()
                        areaViewPager.visibility = View.GONE
                        allNearbyUsers.clear()
                        page = 1
                        nestedScrollPaginationView.resetPageCounter()
                        val map = HashMap<String, String>()
                        map.put("per_page","36")
                        map.put("page",page.toString())
                        map.put("search_text",p0.toString())
                        setCall(map)
                    }
                    else {
                       // areaViewPager.adapter = UserGridAdapter(context, firstResult.copy())
                       // userGridAdapter.update()
                        adapter = UsersAdaptorList(null, allNearbyUsers)
                        areaViewPager.adapter = adapter//userGridAdapter
                        adapter.notifyDataSetChanged()

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

    override fun onLoadMore(currentPage: Int) {
        if (currentPage < (lastPage?:0)) {
            val map = HashMap<String, String>()
            map.put("page", currentPage.toString())
            map.put("per_page", "36")
            shimmerWait.visible()
            shimmerWaitUsers.visible()
            setCall(map)
        }
    }


}