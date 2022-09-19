package com.urcloset.smartangle.dialog

import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.searchActivity.ISearchActivity
import com.urcloset.smartangle.adapter.project105.ChooseColorAdapter
import com.urcloset.smartangle.adapter.project105.SpacesItemDecorationV2
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.RateModel
import com.urcloset.smartangle.model.project_105.ColorModelHassan
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RateDialog (val context: TemplateActivity,
                  val id:String

)
    : Dialog(context) {


    var disposable = CompositeDisposable()

    lateinit var rating:RatingBar
    lateinit var rate: CardView
    lateinit var cancel: CardView
    lateinit var shimmerRate:ShimmerFrameLayout




    init {


        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.chose_dialog)
        initView()
        initEvent()


    }
    fun startLoading(){
        shimmerRate.visible()
        rate.visibility = View.GONE


    }
    fun endLoading(){
        shimmerRate.hide()
        shimmerRate.visibility =  View.GONE
        rate.visibility = View.VISIBLE
        this.dismiss()


    }



    fun rateProduct(rate: String) {
        if (BasicTools.isConnected(context)) {
            startLoading()
            val shopApi =
                ApiClient.getClientJwt(
                    TemplateActivity.loginResponse?.data?.accessToken!!,
                    BasicTools.getProtocol(context).toString(), "en"
                )
                    ?.create(
                        AppApi::class.java
                    )
            val map = HashMap<String, String>()
            map.put("rate", rate)
            map.put("rated_id",id)
            map.put("review", "no review")
            val observable = shopApi!!.rate(map)
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<RateModel>(context) {
                        override fun onSuccess(result: RateModel) {
                            endLoading()
                            if (result.status!!) {

                                context.showToastMessage(result.messages)


                            }
                            else{
                                context.showToastMessage(R.string.faild)

                            }
                        }


                        override fun onFailed(status: Int) {
                            context.showToastMessage(R.string.faild)


                            endLoading()

                        }
                    })
            )

        } else {
            context.showToastMessage(R.string.no_connection)

        }
    }


    private fun initView() {
        cancel=findViewById<CardView>(R.id.cancel)
        rate = findViewById<CardView>(R.id.rate)
        rating = findViewById<RatingBar>(R.id.rating)
        shimmerRate= findViewById(R.id.shimmer_rate)





    }


    private fun initEvent() {


        cancel.setOnClickListener{
            dismiss()

        }

        rate.setOnClickListener {
            rateProduct(rating.rating.toString())



        }
    }



















}