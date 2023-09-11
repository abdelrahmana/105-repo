package com.urcloset.smartangle.activity.resetPasswrod


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.signUp.CompleteDataAct
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.databinding.ActivityVCodeBinding
import com.urcloset.smartangle.model.project_105.CheckOtpEmailModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ResetPasswordEmailCode : TemplateActivity() {

    lateinit var ivBack: ImageView
    var disposable: CompositeDisposable = CompositeDisposable()

    var binding : ActivityVCodeBinding?=null
    override fun set_layout() {
        binding = ActivityVCodeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
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
       binding!!.btnNext.setOnClickListener {
            if(binding!!.editCode.text.trim().isEmpty())
                showToastMessage(R.string.enter_code)

            else {
                checkCode()

            }



        }

    }

    override fun set_fragment_place() {

    }

    fun checkCode(){
        if (BasicTools.isConnected(this@ResetPasswordEmailCode)) {



            BasicTools.showShimmer(binding!!.btnNext ,binding!!.shimmerWait,true)

            var map = HashMap<String, String>()



            map.put("email",TemplateActivity.signupEmail)
            map.put("code",binding!!.editCode.text.trim().toString())




            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@ResetPasswordEmailCode)
                ,  BasicTools.getProtocol(this@ResetPasswordEmailCode).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.checkEmailOtp(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<CheckOtpEmailModel>(this@ResetPasswordEmailCode) {
                        override fun onSuccess(result: CheckOtpEmailModel) {


                            BasicTools.showShimmer(binding!!.btnNext,binding!!.shimmerWait,false)


                            TemplateActivity.codeForEmailOrPhone=binding!!.editCode.text.trim().toString()

                            BasicTools.openActivity(this@ResetPasswordEmailCode,
                                ResetPasswordNewPass::class.java,true)

                            if(!result.data!!.statusCheck!!){


                            }

                        }
                        override fun onFailed(status: Int) {

                            BasicTools.showShimmer(binding!!.btnNext,binding!!.shimmerWait,false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {

            showToastMessage(R.string.no_connection)}
    }


}
