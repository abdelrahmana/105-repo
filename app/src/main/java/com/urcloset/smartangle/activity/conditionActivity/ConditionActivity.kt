package com.urcloset.smartangle.activity.conditionActivity


import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.smartangle.R

import com.urcloset.smartangle.adapter.project105.CategoryConditionAdapter
import com.urcloset.smartangle.adapter.project105.ConditionBookMarkAdapter
import com.urcloset.smartangle.adapter.project105.SpacesItemDecoration
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.BookMarkModelV2
import com.urcloset.smartangle.model.project_105.BookmarkMV3
import com.urcloset.smartangle.model.project_105.CategoryModelV2
import com.urcloset.smartangle.model.project_105.ConditionBookMarkModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ConditionActivity : TemplateActivity(),IConditionActivity {


    var disposableCategory: CompositeDisposable = CompositeDisposable()
    var disposableCondtion: CompositeDisposable = CompositeDisposable()

    lateinit var rvCondition:RecyclerView
    lateinit var rvCategories:RecyclerView


    lateinit var shimmerCondition:ShimmerFrameLayout
    lateinit var shimmerCategories:ShimmerFrameLayout
    lateinit var shimmerSearch:ShimmerFrameLayout


    lateinit var adapterCondition:ConditionBookMarkAdapter
    lateinit var adapterCategory:CategoryConditionAdapter


    lateinit var ivBack:ImageView
    lateinit var editSearch:EditText


    lateinit var layoutManagerCondition: GridLayoutManager
    lateinit var layoutManagerCategories: GridLayoutManager

    lateinit var btnSearch:CardView


    override fun set_layout() {
        setContentView(R.layout.activity_condition_)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

        btnSearch=findViewById(R.id.btn_search)

        rvCondition=findViewById(R.id.rv_conditions)
        rvCategories=findViewById(R.id.rv_categories)

        layoutManagerCondition=GridLayoutManager(this,3)

        layoutManagerCategories=GridLayoutManager(this,3)

        shimmerCondition=findViewById(R.id.shimmer_condition)
        shimmerCategories=findViewById(R.id.shimmer_category)
        shimmerSearch=findViewById(R.id.shimmer_wait)

        ivBack=findViewById(R.id.iv_back)

        adapterCategory= CategoryConditionAdapter(this,ArrayList(),this,
            TemplateActivity.bookmarkConditionSelectedCategoryIndex)
        adapterCondition= ConditionBookMarkAdapter(this,ArrayList(),this,
        TemplateActivity.bookmarkConditionSelectedCondtionInedx)



        rvCondition.adapter=adapterCondition
        rvCategories.adapter=adapterCategory

        rvCondition.layoutManager=layoutManagerCondition
        rvCategories.layoutManager=layoutManagerCategories

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.d2)
        val spacingInPixels2 = resources.getDimensionPixelSize(R.dimen.d0_2)
        rvCategories.addItemDecoration( SpacesItemDecoration(spacingInPixels2))
        rvCondition.addItemDecoration( SpacesItemDecoration(spacingInPixels2))


    }

    override fun init_events() {

        ivBack.setOnClickListener {
            BasicTools.exitActivity(this)
        }



        btnSearch.setOnClickListener {

            if(adapterCategory.getList().isNotEmpty() &&adapterCondition.getList().isNotEmpty())
            getConditionResult()
        }

        getCondition()
        getCategories()

    }

    override fun set_fragment_place() {

    }



    fun getCategories(){
        if (BasicTools.isConnected(this@ConditionActivity)) {



            BasicTools.showShimmer(rvCategories,shimmerCategories,true)

            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@ConditionActivity).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.getCategoryWithPaginate()
            disposableCategory.clear()
            disposableCategory.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CategoryModelV2>(this@ConditionActivity) {
                        override fun onSuccess(result: CategoryModelV2) {
                            BasicTools.showShimmer(rvCategories,shimmerCategories,false)

                            if(result.status!!) {


                                adapterCategory.change_data(result.data?.data!!)


                            }







                        }
                        override fun onFailed(status: Int) {
                            BasicTools.showShimmer(rvCategories,shimmerCategories,false)
                            showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            showToastMessage(R.string.no_connection)}
    }



    fun getCondition(){
        if (BasicTools.isConnected(this@ConditionActivity)) {



            BasicTools.showShimmer(rvCondition,shimmerCondition,true)

            val shopApi = ApiClient.getClient(BasicTools.getProtocol(this@ConditionActivity).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.getCondition()
            disposableCondtion.clear()
            disposableCondtion.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<ConditionBookMarkModel>(this@ConditionActivity) {
                        override fun onSuccess(result: ConditionBookMarkModel) {
                            BasicTools.showShimmer(rvCondition,shimmerCondition,false)

                            if(result.status!!) {

                                adapterCondition.change_data(result.data?.data!!)



                            }







                        }
                        override fun onFailed(status: Int) {
                            BasicTools.showShimmer(rvCondition,shimmerCondition,false)
                            showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            showToastMessage(R.string.no_connection)}
    }




    fun getConditionResult(){
        if (BasicTools.isConnected(this@ConditionActivity)) {




            var map=HashMap<String,String>()
            map.put("condition_id",adapterCondition.getSelectedItem().id.toString())
            map.put("category_id",adapterCategory.getSelectedItem().id.toString())
            map.put("with_paginate","yes")

            if(TemplateActivity.bookmarkSeachText.isNotEmpty())
                map.put("search_text",TemplateActivity.bookmarkSeachText)

            BasicTools.showShimmer(btnSearch,shimmerSearch,true)

            var token="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIzIiwianRpIjoiMWUyZjlmNTEwNzlmZGFlYWQ2Nzc2NmE2NTI2Nzc4ZmM3M2YxZTUyZTE0OWY2MWNlNWQwNWE4ZjFjZDQ3NDMzMzg4MDU5MTYwNDViYjJiNmYiLCJpYXQiOjE2MzMzNzQyODkuMTQwODI5MDg2MzAzNzEwOTM3NSwibmJmIjoxNjMzMzc0Mjg5LjE0MDgzNjk1NDExNjgyMTI4OTA2MjUsImV4cCI6MTY2NDkxMDI4OS4xMzEwNjI5ODQ0NjY1NTI3MzQzNzUsInN1YiI6IjEwIiwic2NvcGVzIjpbXX0.Bda8ytFQZKiWxqNtQkFWa9edqW9g5FBh96lYr9azg5ky2m-E8V5e0luHPcMHlU3N5huPVNA9fegrwr0BDf_yBC18mWYs2XW1qrGEVIUqzs5kI1Mh9OB6L4Aq8v_TmTO8A5E3wE7rMqDUPI5mPKLqvte05RGuSnjktLpINZytfhdF2slBCcvuSuOXz72SqjJWVvFoTNb5IwilGXkBpMjgm80vbOHcjjQy-hf2pEXcimQzGFTOvnSGMFwf0120HPH0ODY6i8UPpovSTjG9KElT4fU5xoW-UxgMuvR5G4SURxSo57LcBlxT1pgFAFmuhsfTJXMJ5198ZN6lEMpMNzTyOFdnN1FitBJCAxGzOR2AFdvzQZXExUfaS798Xv60F0B6_FCVS4kb-55n5-B2_mzsSrOK3Uq5qBeFhdkmXNbMnE_fz2hqUoZlVI4tyN_S2UUU_vdK_woxwLFMjro6Tc-AbUj4hfac1TEAdCDHIvWLGLvHHXucL1so5mSEfmff_EWzX0oqg_fcPkMB3uuHQTBHBccBYEQ7cyoCSlUxenm8-1h4_W_Zkq4mhWXG6ADBMTVBBzx0LueNK1zxAZ9czQPaiDBOrhqqFXKennhzDOIfLvTftei67xvhEnuGxGlsgKhzEc00B2LuR8peb70YyR6U9kT3RfmbDZ3OdqP37mENClY"
            val shopApi = ApiClient.getClientJwt(BasicTools.getToken(this@ConditionActivity)
                ,BasicTools.getProtocol(this@ConditionActivity).toString())
                ?.create(AppApi::class.java)

            val observable= shopApi!!.getBookMark(map)
            disposableCondtion.clear()
            disposableCondtion.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<BookmarkMV3>(this@ConditionActivity) {
                        override fun onSuccess(result: BookmarkMV3) {
                            BasicTools.showShimmer(btnSearch,shimmerSearch,false)

                            if(result.status!!) {




                                lastPageBookMark=result.data!!.lastPage!!

                                if(bookMarkListV2==null)
                                    bookMarkListV2= ArrayList()
                                bookMarkListV2!!.clear()
                                bookMarkListV2!!.addAll(result.data?.data!!)
                                BasicTools.exitActivity(this@ConditionActivity)



                            }







                        }
                        override fun onFailed(status: Int) {
                            BasicTools.showShimmer(btnSearch,shimmerSearch,false)
                            showToastMessage(R.string.faild)

                        }
                    }))
        }
        else {

            showToastMessage(R.string.no_connection)}
    }





}
