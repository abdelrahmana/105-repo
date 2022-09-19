package com.urcloset.smartangle.activity.forgotPassword


import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.urcloset.smartangle.R
import com.urcloset.smartangle.api.ApiClient
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.model.project21.MessageModel
import com.urcloset.smartangle.tools.AppObservable
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.hbb20.CountryCodePicker
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_enter_phone_forgotpassword.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*

class EnterPhoneActivityForgotPassword : TemplateActivity(), Validator.ValidationListener {
    var disposable= CompositeDisposable()
    var validator: Validator? = null
    lateinit var countryCode: CountryCodePicker
    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var editPhone: EditText
    override fun set_layout() {
        setContentView(R.layout.activity_enter_phone_forgotpassword)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

        editPhone=edit_phone
        countryCode=country_code

    }

    override fun init_events() {

        iv_back.setOnClickListener {
            BasicTools.exitActivity(this@EnterPhoneActivityForgotPassword)
        }

        validator = Validator(this)
        validator!!.setValidationListener(this)

        card_contintue.setOnClickListener{
            validator!!.validate()

        }

    }

    override fun set_fragment_place() {

    }

    override fun onValidationFailed(errors:List<ValidationError>?) {
        BasicTools.showValidationErrorMessagesForFields(errors!!, this@EnterPhoneActivityForgotPassword)
    }

    override fun onValidationSucceeded() {


     //   checkPhoneRQ(editPhone.text.trim().toString())

    }


    fun showShimmerBtn(state: Boolean){

        if(state){
            card_contintue.visibility= View.GONE
            shimmer_wait.visible()
        }else{
            card_contintue.visibility= View.VISIBLE
            shimmer_wait.hide()
        }
    }

//    fun checkPhoneRQ(txt:String){
//        if (BasicTools.isConnected(this@EnterPhoneActivityForgotPassword)) {
//
//
//
//            showShimmerBtn(true)
//
//            var map = HashMap<String, String>()
//            map.put("email_phone",txt)
//            val shopApi = ApiClient.getClientJwt(
//                BasicTools.getToken(this@EnterPhoneActivityForgotPassword),
//                BasicTools.getProtocol(this@EnterPhoneActivityForgotPassword).toString())
//                ?.create(AppApi::class.java)
//            val observable= shopApi!!.checkPhone(map)
//            disposable.clear()
//            disposable.add(
//                observable.subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribeWith(object : AppObservable<MessageModel>(this@EnterPhoneActivityForgotPassword) {
//                        override fun onSuccess(result: MessageModel) {
//
//
//                            showShimmerBtn(false)
//
//                            if(result.status!!){
//
//                                TemplateActivity.phoneForgotPasswrod=editPhone.text.trim().toString()
//                                TemplateActivity.emailForgotPasswrod=""
//                                TemplateActivity.countryCodeForgotPasswrod=countryCode.selectedCountryCodeWithPlus
//
//
//                                TemplateActivity.storedVerificationId=""
//                                TemplateActivity.tokenResendOtp=null
//                                BasicTools.openActivity(this@EnterPhoneActivityForgotPassword,EnterCodeForgotPassowrdPhoneActivity::class.java,false)
//
//
//
//                            }else{
//                                showToastMessage(R.string.not_found_phone)
//                            }
//
//
//                        }
//                        override fun onFailed(status: Int) {
//
//                            showShimmerBtn(false)
//                            showToastMessage(R.string.faild)
//                            //BasicTools.logOut(parent!!)
//                        }
//                    }))
//
//        }
//        else {
//            showShimmerBtn(false)
//            showToastMessage(R.string.no_connection)}
//    }


}
