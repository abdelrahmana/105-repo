package com.urcloset.smartangle.activity.privacy

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivityPrivacyBinding
import com.urcloset.smartangle.model.PrivacyModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class PrivacyActivity : TemplateActivity() {
   lateinit var tvPrivacy: TextView
   lateinit var ivBack :ImageView
   lateinit var progress:ProgressBar
   var lang:String ="en"
    var disposable= CompositeDisposable()

    var binding : ActivityPrivacyBinding? =null
    override fun set_layout() {
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

        progress.visibility = View.VISIBLE
        if(!BasicTools.isDeviceLanEn())
            lang ="ar"
        val shopApi =  ApiClient.getClient(BasicTools.getProtocol(applicationContext).toString(),lang)?.create(
            AppApi::class.java)
        val observable= shopApi!!.getGeneralText("terms")
        disposable.clear()
        disposable.add(
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : AppObservable<PrivacyModel>(this) {
                    override fun onSuccess(result: PrivacyModel) {
                        progress.visibility = View.GONE
                        if(result.status!!){
                            progress.visibility = View.GONE

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                if(BasicTools.isDeviceLanEn())
                                tvPrivacy.text = Html.fromHtml(result.data!!.content, Html.FROM_HTML_MODE_COMPACT)
                                else tvPrivacy.text = Html.fromHtml(result.data!!.contentAr, Html.FROM_HTML_MODE_COMPACT)
                            }
                            else{
                                if(BasicTools.isDeviceLanEn())
                                tvPrivacy.text = Html.fromHtml(result.data!!.content)
                                else   tvPrivacy.text = Html.fromHtml(result.data!!.contentAr)

                            }

                        }


                    }
                    override fun onFailed(status: Int) {
                        progress.visibility = View.GONE


                    }
                }))
    }

    override fun init_views() {
        tvPrivacy =binding!!.tvPrivacy
        ivBack = binding!!.toolbar.ivBack
        progress = binding!!.progressPrivacy
    }

    override fun init_events() {
        ivBack.setOnClickListener {
            BasicTools.exitActivity(this)
        }
    }

    override fun set_fragment_place() {
    }
}