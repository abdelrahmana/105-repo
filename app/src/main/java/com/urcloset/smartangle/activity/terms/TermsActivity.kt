package com.urcloset.smartangle.activity.terms

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
import com.urcloset.smartangle.model.PrivacyModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_terms.*
import java.util.*

class TermsActivity : TemplateActivity() {
    lateinit var tvTerms :TextView
    var disposable= CompositeDisposable()
    var lang:String ="en"
    lateinit var ivBack: ImageView
    lateinit var progress:ProgressBar




    override fun set_layout() {
        setContentView(R.layout.activity_terms)
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
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                if(BasicTools.isDeviceLanEn())
                                tvTerms.text = Html.fromHtml(result.data!!.content, Html.FROM_HTML_MODE_COMPACT)
                                else
                                    tvTerms.text = Html.fromHtml(result.data!!.contentAr, Html.FROM_HTML_MODE_COMPACT)
                            }
                            else{

                                if(BasicTools.isDeviceLanEn())
                                tvTerms.text = Html.fromHtml(result.data!!.content)
                                else
                                    tvTerms.text = Html.fromHtml(result.data!!.contentAr)
                            }

                        }


                    }
                    override fun onFailed(status: Int) {
                        progress.visibility = View.GONE


                    }
                }))
    }

    override fun init_views() {
        tvTerms = tv_terms
        ivBack=findViewById(R.id.iv_back)
        progress = findViewById(R.id.progress_bar_terms)

    }

    override fun init_events() {
        ivBack.setOnClickListener {
            finish()
        }
    }

    override fun set_fragment_place() {
    }
}