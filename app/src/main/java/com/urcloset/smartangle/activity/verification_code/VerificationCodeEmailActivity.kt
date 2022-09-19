package com.urcloset.smartangle.activity.verification_code


import android.os.Bundle
import android.widget.ImageView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.signUp.CompleteDataAct
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project_105.CheckOtpEmailModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_v_code.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*

class VerificationCodeEmailActivity : TemplateActivity() {

    lateinit var ivBack:ImageView

    var disposable: CompositeDisposable = CompositeDisposable()

    override fun set_layout() {
        setContentView(R.layout.activity_v_code)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

        ivBack=findViewById(R.id.iv_back)

    }

    override fun init_events() {


        ivBack.setOnClickListener {
            BasicTools.exitActivity(this)
        }

        btn_next.setOnClickListener {



            if(edit_code.text.trim().isEmpty())
                showToastMessage(R.string.enter_code)

            else {
                checkCode()

            }



        }

    }

    override fun set_fragment_place() {

    }

    fun checkCode(){
        if (BasicTools.isConnected(this@VerificationCodeEmailActivity)) {



            BasicTools.showShimmer(btn_next,shimmer_wait,true)

            var map = HashMap<String, String>()



                map.put("email",TemplateActivity.signupEmail)
                map.put("code",edit_code.text.trim().toString())




            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@VerificationCodeEmailActivity)
                ,  BasicTools.getProtocol(this@VerificationCodeEmailActivity).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.checkEmailOtp(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CheckOtpEmailModel>(this@VerificationCodeEmailActivity) {
                        override fun onSuccess(result: CheckOtpEmailModel) {


                            BasicTools.showShimmer(btn_next,shimmer_wait,false)



                            BasicTools.openActivity(this@VerificationCodeEmailActivity,
                            CompleteDataAct::class.java,true)

                            if(!result.data!!.statusCheck!!){


                            }

                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(btn_next,shimmer_wait,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {

            showToastMessage(R.string.no_connection)}
    }


}
