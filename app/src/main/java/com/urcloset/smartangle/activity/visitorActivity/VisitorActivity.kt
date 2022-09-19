package com.urcloset.smartangle.activity.visitorActivity

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.VisitorAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.NotificationModel
import com.urcloset.smartangle.model.VisitorModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_visitor.*
import java.util.*
import kotlin.collections.ArrayList

class VisitorActivity : TemplateActivity() {
    lateinit var rvVisitors:RecyclerView
    var visitorAdapter : VisitorAdapter?=null
    var disposable= CompositeDisposable()
    lateinit var shimmerVisitor : ShimmerFrameLayout
    var visitorModel: VisitorModel?=null
    lateinit var ivBack: ImageView
    var isLoading =false
    var currentPage =1
    lateinit var progressBar: ProgressBar





    override fun set_layout() {
            setContentView(R.layout.activity_visitor)

    }

    override fun init_activity(savedInstanceState: Bundle?) {
        if(BasicTools.isConnected(this)) {
            BasicTools.showShimmer(rvVisitors, shimmerVisitor, true)


            val shopApi = ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(this@VisitorActivity).toString()
            )?.create(
                AppApi::class.java
            )
            val observable = shopApi!!.getVisitors(page = currentPage.toString())
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<VisitorModel>(this) {
                        override fun onSuccess(result: VisitorModel) {
                            BasicTools.showShimmer(rvVisitors, shimmerVisitor, false)
                            shimmerVisitor.visibility = View.GONE

                            if (result.status == true) {
                                visitorModel = result

                                visitorAdapter = VisitorAdapter(this@VisitorActivity,
                                    result.data?.users?.data as ArrayList<VisitorModel.Data.Users.Data>
                                )

                                rvVisitors.layoutManager = LinearLayoutManager(
                                    this@VisitorActivity,
                                    LinearLayoutManager.VERTICAL, false
                                )
                                rvVisitors.adapter = visitorAdapter
                                if(BasicTools.isDeviceLanEn())
                                tv_visitor.text =
                                    "Visitors (" + result.data?.users?.total.toString() + ")"
                                else  tv_visitor.text =
                                    "الزوار (" + result.data?.users?.total.toString() + ")"
                                if (result.data?.users?.data?.size!! <= 0) {
                                    findViewById<LinearLayout>(R.id.ly_empty).visibility =
                                        View.VISIBLE

                                } else {
                                    findViewById<LinearLayout>(R.id.ly_empty).visibility = View.GONE

                                }


                            }



                        }

                        override fun onFailed(status: Int) {
                            BasicTools.showShimmer(rvVisitors, shimmerVisitor, false)
                            shimmerVisitor.visibility = View.GONE


                        }
                    })

            )
        }
        else {
            showToastMessage(R.string.no_connection)
        }
        ivBack.setOnClickListener {

            BasicTools.exitActivity(this)
        }
    }
   fun getNextVisitors(page:String){
       isLoading = true
       val shopApi =  ApiClient.getClientJwt(
           TemplateActivity.loginResponse?.data?.accessToken!!,
           BasicTools.getProtocol(applicationContext).toString(), "en"
       )?.create(
           AppApi::class.java
       )
       val observable= shopApi!!.getVisitors(page)
       disposable.clear()
       disposable.add(
           observable.subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(object : AppObservable<VisitorModel>(applicationContext) {
                   override fun onSuccess(result: VisitorModel) {
                       isLoading = false
                       if (result.status!!) {
                           progressBar.visibility = View.GONE

                           result.data?.users?.data?.forEach {
                            visitorAdapter?.addItem(it!!)

                           }


                       }


                   }

                   override fun onFailed(status: Int) {
                       isLoading = false
                       progressBar.visibility = View.GONE



                   }
               })

       )

    }

    override fun init_views() {
        rvVisitors = recyclerview_visitors
        shimmerVisitor = shimmer_visitor
        ivBack=findViewById(R.id.iv_back)
        progressBar = findViewById(R.id.progress)



    }

    override fun init_events() {
        rvVisitors.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(!isLoading) {
                        if (currentPage < visitorModel?.data?.users?.lastPage!!) {
                            progressBar.visibility = View.VISIBLE
                            currentPage = currentPage + 1

                            getNextVisitors(currentPage.toString())
                        }
                    }


                }
            }
        })
    }

    override fun set_fragment_place() {
    }

}