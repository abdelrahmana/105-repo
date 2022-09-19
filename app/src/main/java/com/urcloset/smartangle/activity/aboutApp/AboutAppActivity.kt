package com.urcloset.smartangle.activity.aboutApp

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.urcloset.smartangle.R
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.PrivacyModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_about_app.*
import java.util.*

class AboutAppActivity : TemplateActivity() {
    var disposable= CompositeDisposable()
    lateinit var about:TextView
    lateinit var aboutProgress:ProgressBar
    lateinit var ivBack :ImageView
    lateinit var logo :ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun set_layout() {
        setContentView(R.layout.activity_about_app)

        }

    override fun init_activity(savedInstanceState: Bundle?) {
        val shopApi =  ApiClient.getClient(
            BasicTools.getProtocol(applicationContext).toString(),
            "en"
        )?.create(
            AppApi::class.java
        )
        val observable= shopApi!!.getGeneralText("about_app")
        aboutProgress.visibility = View.VISIBLE
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<PrivacyModel>(this) {
                    override fun onSuccess(result: PrivacyModel) {
                        aboutProgress.visibility = View.GONE

                        if (result.status!!) {
                            if (!result.data?.mediaPath.isNullOrEmpty())
                                Glide.with(this@AboutAppActivity).load(
                                    Uri.decode(result.data?.fullPath)
                                ).into(logo)
                            else logo.visibility = View.GONE
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                if (BasicTools.isDeviceLanEn())
                                    about.text = Html.fromHtml(
                                    result.data!!.content,
                                        Html.FROM_HTML_MODE_COMPACT
                                    )
                                else about.text = Html.fromHtml(
                                    result.data!!.contentAr,
                                    Html.FROM_HTML_MODE_COMPACT
                                )

                            } else {
                                if (BasicTools.isDeviceLanEn())
                                    about.text = Html.fromHtml(result.data!!.content)
                                else about.text = Html.fromHtml(result.data!!.contentAr)

                            }

                        }


                    }

                    override fun onFailed(status: Int) {
                        aboutProgress.visibility = View.GONE


                    }
                })
        )
    }

    override fun init_views() {
        about = tv_about
        aboutProgress = about_progress
        ivBack = findViewById(R.id.iv_back)
        logo = findViewById(R.id.logo_img)
    }

    override fun init_events() {
        ivBack.setOnClickListener {
            BasicTools.exitActivity(this)
        }

    }

    override fun set_fragment_place() {
    }
    fun convertToHtml(htmlString: String?): String? {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<![CDATA[")
        stringBuilder.append(htmlString)
        stringBuilder.append("]]>")
        return stringBuilder.toString()
    }
}