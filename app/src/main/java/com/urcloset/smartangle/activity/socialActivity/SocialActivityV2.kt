package com.urcloset.smartangle.activity.socialActivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.socialActivity.adaptor.SocialLinkAdaptor
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivitySocialIiBinding
import com.urcloset.smartangle.fragment.HomeFragment.adaptor.UsersAdaptorList
import com.urcloset.smartangle.model.*
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.GridModel
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_social.*
import java.lang.Exception


class SocialActivityV2 : TemplateActivity() {

    var disposable = CompositeDisposable()
    lateinit var binding : ActivitySocialIiBinding

    override fun set_layout() {
        binding = ActivitySocialIiBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun init_activity(savedInstanceState: Bundle?) {
        binding.progressSocial.visibility = View.VISIBLE
        val shopApi =
            ApiClient.getClientJwt(
                TemplateActivity.loginResponse?.data?.accessToken!!,
                BasicTools.getProtocol(applicationContext).toString(), "en"
            )
                ?.create(
                    AppApi::class.java
                )

        val observable = shopApi!!.getSocialLoginData(HashMap<String,Any>().also {
            it.put("with_paginate","no")
        })
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<GlobalLink>(this) {
                    override fun onSuccess(result: GlobalLink) {
                        if (result.status!!) {
                            binding.progressSocial.visibility = View.GONE

                            val socialLinks = result.data!!
                           val adapter = SocialLinkAdaptor(itemClickedCallBack,
                               (socialLinks?:ArrayList()) as ArrayList<Datax>
                           )
                            BasicTools.setRecycleView(binding.links,adapter,
                                LinearLayoutManager.HORIZONTAL,this@SocialActivityV2,
                                null,false)

                        }
                    }

                    override fun onFailed(status: Int) {
                        binding.progressSocial.visibility = View.GONE
                        showToastMessage(R.string.faild)

                        super.onFailed(status)
                    }
                }
                ))
    }
val itemClickedCallBack  :(Datax)->Unit = {
    BasicTools.open_website(it.value.toString(),this@SocialActivityV2)

}


    override fun init_views() {

    }
    override fun init_events() {
       binding.header.ivBack.setOnClickListener {

            BasicTools.exitActivity(this)
        }

    }

    override fun set_fragment_place() {
    }

}