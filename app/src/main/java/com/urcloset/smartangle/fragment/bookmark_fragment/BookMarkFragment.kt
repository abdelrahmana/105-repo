package com.urcloset.smartangle.fragment.bookmark_fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.conditionActivity.ConditionActivity
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.activity.homeActivity.HomeViewModel
import com.urcloset.smartangle.activity.notificationActivity.NotificationActivity
import com.urcloset.smartangle.adapter.project105.ConditionListAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.FragmentBookMarkBinding
import com.urcloset.smartangle.model.project_105.BookmarkMV3
import com.urcloset.smartangle.tools.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody

class BookMarkFragment : TemplateFragment(),IBookMarkFragment {

    var disposableCondtion: CompositeDisposable = CompositeDisposable()
    lateinit var swip:SwipeRefreshLayout
    lateinit var rv:RecyclerView
    lateinit var ivFilter:ImageView
    lateinit var editSearch:EditText
     var adapter: ConditionListAdapter?=null
    lateinit var shimmer:ShimmerFrameLayout
    lateinit var layoutManager:LinearLayoutManager
    var requestBegin=false
    var page=0
    var lastPage=-1
    lateinit var prgs:ProgressBar

    lateinit var views : View
    lateinit var rootEmpty:LinearLayout
    var binding : FragmentBookMarkBinding? =null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookMarkBinding.inflate(layoutInflater,container,false)
         views = binding!!.root//inflater.inflate(R.layout.fragment_book_mark, container, false)

        viewModelHome.setPreviousNavBottom(R.id.bookmark)

        return views
    }


    override fun init_views() {
        binding?.ivNotification?.setOnClickListener{
            if (TemplateActivity.loginResponse?.data?.accessToken!=null) {
                TemplateActivity.isNotificationVisited =1
                BasicTools.openActivity(parent!!, NotificationActivity::class.java, false)
            }
        }
        swip=views!!.findViewById(R.id.swip_bookmark)
        rv=views!!.findViewById(R.id.rv_bookmark)
        ivFilter=views!!.findViewById(R.id.iv_search)
        adapter=ConditionListAdapter(views.context!!,ArrayList(),this)
        shimmer=views!!.findViewById(R.id.shimmer_wait)

        prgs=views!!.findViewById(R.id.prgs_bookmark)
        rootEmpty=views!!.findViewById(R.id.root_empty_page_bookmark)
        layoutManager= LinearLayoutManager(views.context!!,LinearLayoutManager.VERTICAL,false)
        editSearch=views!!.findViewById(R.id.edit_search_book)
        rv.adapter=adapter
        rv.layoutManager=layoutManager




        rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(!requestBegin &&lastPage!=page)
                   getConditionResult()
                }
            }
        })





    }


    override fun onResume() {
        super.onResume()
        if(adapter!=null&& TemplateActivity.bookMarkListV2!=null){
            page=1
            lastPage= TemplateActivity.lastPageBookMark
        adapter?.change_data(TemplateActivity.bookMarkListV2!!)
        }
     /*   if(HomeActivity.bottomNavigation?.currentItem!=1){
            HomeActivity.doNothing =  true
            HomeActivity.bottomNavigation?.currentItem=1
            HomeActivity.doNothing =  false

        }*/

    }

    override fun init_events() {



        ivFilter.setOnClickListener {


            BasicTools.openActivity(parent!!, ConditionActivity::class.java,false)
        }




        if(TemplateActivity.bookMarkListV2==null)
            getConditionResult()
        else
            adapter!!.change_data(TemplateActivity.bookMarkListV2!!)










        swip.setOnRefreshListener {

            page=0
            lastPage=-1

            TemplateActivity.bookMarkListV2=null

            TemplateActivity.categoryIdForBookMark=0
            TemplateActivity.conditionIdForBookMark=0

            editSearch.setText("")



          //  getConditionResult()
        }


        editSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                TemplateActivity.bookmarkSeachText=s?.trim().toString()
                page=0
                lastPage=-1
                TemplateActivity.bookMarkListV2=null
                getConditionResult()

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

    }

    override fun init_fragment(savedInstanceState: Bundle?) {

    }

    override fun on_back_pressed(): Boolean {
        return true
    }


    override fun delete(id: Int,pos:Int) {
        deleteBookMark(id.toString(),pos)
    }

    override fun checkIfAdapterEmpty(item: Boolean) {
        if(item){

            rootEmpty.visibility=View.VISIBLE

        }else{
            rootEmpty.visibility=View.GONE
        }
    }

    fun getConditionResult(){
        if (BasicTools.isConnected(parent!!)) {




            requestBegin=true




            var map=HashMap<String,String>()
            map.put("with_paginate","yes")
            map.put("page",(++page).toString())

            if(TemplateActivity.conditionIdForBookMark>0)
            map.put("condition_id",TemplateActivity.conditionIdForBookMark.toString())

            if(TemplateActivity.categoryIdForBookMark>0)
            map.put("category_id",TemplateActivity.categoryIdForBookMark.toString())


            if(TemplateActivity.bookmarkSeachText.isNotEmpty())
            map.put("search_text",TemplateActivity.bookmarkSeachText)


            checkIfAdapterEmpty(false)




            if(page==1){
            BasicTools.showShimmer(rv,shimmer,true)
            prgs.visibility=View.GONE
            }else{
                prgs.visibility=View.VISIBLE
            }

            val shopApi = ApiClient.getClientJwt(BasicTools.getToken(parent!!)
                ,BasicTools.getProtocol(parent!!).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.getBookMark(map)
            disposableCondtion.clear()
            disposableCondtion.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<BookmarkMV3>(parent!!) {
                        override fun onSuccess(result: BookmarkMV3) {
                            BasicTools.showShimmer(rv,shimmer,false)

                            page=result.data!!.currentPage!!
                            lastPage=result.data!!.lastPage!!
                            prgs.visibility=View.GONE
                            requestBegin=false
                            swip.isRefreshing=false
                            if(result.status!!) {
                                if(TemplateActivity.bookMarkListV2==null){
                                    TemplateActivity.bookMarkListV2= ArrayList()
                                TemplateActivity.bookMarkListV2?.clear()
                                TemplateActivity.bookMarkListV2?.addAll(result.data!!.data!!)
                                    adapter!!.change_data(result.data!!.data!!)
                                }else{
                                    TemplateActivity.bookMarkListV2!!.addAll(result.data!!.data!!)
                                    adapter!!.addRefersh(result.data!!.data!!)
                                }




                            }







                        }
                        override fun onFailed(status: Int) {
                            requestBegin=false
                            prgs.visibility=View.GONE
                            BasicTools.showShimmer(rv,shimmer,false)
                            parent!!.showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            parent!!.showToastMessage(R.string.no_connection)}
    }

    fun deleteBookMark(id:String,pos:Int){
        if (BasicTools.isConnected(parent!!)) {



            //page+=1

            var map=HashMap<String,String>()
            map.put("operation_type","0")
            map.put("model_id",id)
            map.put("model_name","Items")

            BasicTools.showShimmer(rv,shimmer,true)

            val shopApi = ApiClient.getClientJwt(BasicTools.getToken(parent!!)
                ,BasicTools.getProtocol(parent!!).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.addDeleteBookMark(map)
            disposableCondtion.clear()
            disposableCondtion.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ResponseBody>(parent!!) {
                        override fun onSuccess(result: ResponseBody) {
                            BasicTools.showShimmer(rv,shimmer,false)

                            swip.isRefreshing=false


                            adapter!!.getList().removeAt(pos)
                            adapter!!.notifyDataSetChanged()



                            getConditionResultForPages()







                        }
                        override fun onFailed(status: Int) {
                            BasicTools.showShimmer(rv,shimmer,false)
                            parent!!.showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            parent!!.showToastMessage(R.string.no_connection)}
    }


    fun getConditionResultForPages(){
        if (BasicTools.isConnected(parent!!)) {




            requestBegin=true




            var map=HashMap<String,String>()
            map.put("with_paginate","yes")
            map.put("page",(page).toString())


                BasicTools.showShimmer(rv,shimmer,true)
                prgs.visibility=View.GONE

                prgs.visibility=View.VISIBLE


            val shopApi = ApiClient.getClientJwt(BasicTools.getToken(parent!!)
                ,BasicTools.getProtocol(parent!!).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.getBookMark(map)
            disposableCondtion.clear()
            disposableCondtion.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<BookmarkMV3>(parent!!) {
                        override fun onSuccess(result: BookmarkMV3) {
                            BasicTools.showShimmer(rv,shimmer,false)

                            page=result.data!!.currentPage!!
                            lastPage=result.data!!.lastPage!!
                            prgs.visibility=View.GONE
                            requestBegin=false
                            swip.isRefreshing=false








                        }
                        override fun onFailed(status: Int) {
                            requestBegin=false
                            prgs.visibility=View.GONE
                            BasicTools.showShimmer(rv,shimmer,false)
                            parent!!.showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            parent!!.showToastMessage(R.string.no_connection)}
    }



}