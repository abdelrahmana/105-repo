package com.urcloset.smartangle.activity.notificationActivity

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.system.Os.bind
import android.util.Base64.encodeToString
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.NotificationAdapter
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.NotificationModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList
import android.util.Base64
import android.widget.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.urcloset.smartangle.activity.productDetails.ProductDetails
import com.urcloset.smartangle.databinding.ActivityNotificationBinding
import com.urcloset.smartangle.listeners.ItemClickListener
import com.urcloset.smartangle.model.BasicModel
import dagger.hilt.android.AndroidEntryPoint
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Inject

@AndroidEntryPoint
class NotificationActivity : TemplateActivity(),ItemClickListener {
    lateinit var rvNotifications:RecyclerView
    var disposable= CompositeDisposable()
    lateinit var notificationModel: NotificationModel
     var currentPage =1
    lateinit var progressBar: ProgressBar
    lateinit var notificationAdapter:NotificationAdapter
    lateinit var shimmerVisitor : ShimmerFrameLayout
    var isLoading =false
    @Inject lateinit var progressDialog :Dialog

    lateinit var ivBackPress:ImageView
    val model : NotificationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModelObservers()

    }

    var binding : ActivityNotificationBinding?=null
    override fun onResume() {
        super.onResume()
        currentPage = 1
        getNotifications(currentPage.toString())

    }

    override fun set_layout() {
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

    }

    override fun init_activity(savedInstanceState: Bundle?) {
        rvNotifications.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        getNotifications(currentPage.toString())
        binding?.deleteAll?.setOnClickListener{
            if (BasicTools.isConnected(this))
            // delete all
            model.deleteAllNotifications(HashMap<String, Any>().also { it.put("type","all") })
            else
                BasicTools.showSnackMessages(this@NotificationActivity, getString(R.string.no_connection))
        }
    }

    override fun init_views() {
        rvNotifications = binding!!.rvNotifications
        progressBar = findViewById(R.id.progress)
        shimmerVisitor = binding!!.shimmerVisitor
        ivBackPress=findViewById(R.id.iv_back)


    }
    fun setViewModelObservers() {
        model.deleteAllNotifications.observe(this, androidx.lifecycle.Observer{ // for sending to maintenance
            if (it!=null){
                Toast.makeText(this@NotificationActivity, getString(R.string.all_notifcations_deleted), Toast.LENGTH_SHORT).show()
                model.deleteAllNotifications(null)
            }

        })
        model.networkLoader.observe(this@NotificationActivity, Observer{
            it?.let { progress->
                progress.setDialog(progressDialog) // open close principles
                model.setNetworkLoader(null)
            }
        })

        model.errorViewModel.observe(this@NotificationActivity) {
            it?.let { error ->
                BasicTools.showSnackMessages(this@NotificationActivity, error)

            }
        }
    }
    override fun init_events() {
        ivBackPress.setOnClickListener {
            onBackPressed()
        }
        rvNotifications.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    if(!isLoading) {
                        if (currentPage < notificationModel.data?.list?.lastPage!!) {
                            progressBar.visibility = View.VISIBLE
                            currentPage = currentPage + 1

                            getNextNotifications(currentPage.toString())
                        }
                    }


                }
            }
        })
    }

    override fun set_fragment_place() {
    }
    fun getNotifications(page:String){
        BasicTools.showShimmer(rvNotifications,shimmerVisitor,true)
        val shopApi =  ApiClient.getClientJwt(
            TemplateActivity.loginResponse?.data?.accessToken!!,
            BasicTools.getProtocol(applicationContext).toString(), "en"
        )?.create(
            AppApi::class.java
        )
        val observable= shopApi!!.getNotifications(page)
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<NotificationModel>(applicationContext) {
                    override fun onSuccess(result: NotificationModel) {
                        BasicTools.showShimmer(rvNotifications,shimmerVisitor,false)
                        shimmerVisitor.visibility = View.GONE

                        if (result.status!!) {
                            progressBar.visibility = View.GONE

                            notificationModel = result
                            if(result.data?.list?.notifications?.size!!>0) {
                                findViewById<TextView>(R.id.tv_no_data).visibility = View.GONE

                                notificationAdapter = NotificationAdapter(
                                    this@NotificationActivity,
                                    result.data.list.notifications as ArrayList<NotificationModel.Data.NotificationList.NotificationItem>
                                )
                                notificationAdapter.setOnItemClickListener(this@NotificationActivity)
                                rvNotifications.adapter = notificationAdapter
                                binding?.deleteAll?.visibility = View.VISIBLE
                            }
                            else{
                                findViewById<LinearLayout>(R.id.ly_empty).visibility = View.VISIBLE

                            }


                        }


                    }

                    override fun onFailed(status: Int) {
                        progressBar.visibility = View.GONE
                        shimmerVisitor.visibility = View.GONE

                        BasicTools.showShimmer(rvNotifications,shimmerVisitor,false)





                    }
                })

        )


    }
    fun getNextNotifications(page:String){
        isLoading = true
        val shopApi =  ApiClient.getClientJwt(
            TemplateActivity.loginResponse?.data?.accessToken!!,
            BasicTools.getProtocol(applicationContext).toString(), "en"
        )?.create(
            AppApi::class.java
        )
        val observable= shopApi!!.getNotifications(page)
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<NotificationModel>(applicationContext) {
                    override fun onSuccess(result: NotificationModel) {
                        isLoading = false
                        if (result.status!!) {
                            progressBar.visibility = View.GONE

//                            notificationModel = result
                            result.data?.list?.notifications?.forEach {
                                notificationAdapter.addNewItem(it!!)

                            }
//                            val  ids = ArrayList<String>()
//                            result.data?.list?.notifications?.forEach {
//                                ids.add(it?.id.toString())
//
//                            }
//                            readNotifications(ids)

                        }


                        }




                    override fun onFailed(status: Int) {
                        isLoading = false
                        progressBar.visibility = View.GONE



                    }
                })

        )


    }
fun readNotifications(ids:ArrayList<String>){
    val shopApi =  ApiClient.getClientJwt(
        TemplateActivity.loginResponse?.data?.accessToken!!,
        BasicTools.getProtocol(applicationContext).toString(), "en"
    )?.create(
        AppApi::class.java
    )
    val observable= shopApi!!.readNotification(ids)
    disposable.clear()
    disposable.add(
        observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : AppObservable<BasicModel>(applicationContext) {
                override fun onSuccess(result: BasicModel) {
                    Log.d("notificationHome", result.messages.toString())


                }

                override fun onFailed(status: Int) {



                }
            })

    )

}

    override fun onClick(position: Int) {
     val notification =   notificationAdapter.notifications?.get(position)
        val product = notification?.item
        val ids= ArrayList<String>()



        if(product!=null&&product.id!=null){
        val intent = Intent(this, ProductDetails::class.java)
        val gson = Gson()
        intent.putExtra("product", gson.toJson(product))
         startActivity(intent)}
        ids.add(notification?.id.toString())
        readNotifications(ids)
    }

}
