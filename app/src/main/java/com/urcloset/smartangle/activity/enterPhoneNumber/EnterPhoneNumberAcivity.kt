package com.urcloset.smartangle.activity.enterPhoneNumber


import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import com.mobsandgeeks.saripaar.annotation.NotEmpty

import com.urcloset.smartangle.R
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import com.urcloset.shop.tools.hide
import com.urcloset.shop.tools.visible
import com.hbb20.CountryCodePicker
import com.urcloset.smartangle.databinding.ActivityEnterPhoneNumberBinding
import io.reactivex.disposables.CompositeDisposable

class EnterPhoneNumberAcivity : TemplateActivity(), Validator.ValidationListener {
    var disposable= CompositeDisposable()
    lateinit var countryCode: CountryCodePicker
    var validator: Validator? = null

    var binding : ActivityEnterPhoneNumberBinding ? =null
    @NotEmpty(sequence = 1, messageResId = R.string.please_fill_out)
    lateinit var editphone: EditText
    override fun set_layout() {
        binding = ActivityEnterPhoneNumberBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }

    override fun init_views() {

        countryCode=binding!!.countryCode
        editphone=binding!!.editPhone
    }



    override fun init_events() {


        binding?.toolbar?.ivBack?.setOnClickListener {
            BasicTools.exitActivity(this@EnterPhoneNumberAcivity)
        }
        validator = Validator(this)
        validator!!.setValidationListener(this)


        binding?.cardContintue?.setOnClickListener{
            validator!!.validate()

        }



    }

    override fun set_fragment_place() {

    }

    override fun onValidationFailed(errors:List<ValidationError>?) {
        BasicTools.showValidationErrorMessagesForFields(errors!!, this@EnterPhoneNumberAcivity)
    }

    override fun onValidationSucceeded() {
      //  checkPhoneRQ(editphone.text.trim().toString())

    }


    fun showShimmerBtn(state: Boolean){

        if(state){
            binding?.cardContintue?.visibility= View.GONE
            binding?.shimmerWait?.visible()
        }else{
            binding?.cardContintue?.visibility= View.VISIBLE
            binding?.shimmerWait?.hide()
        }
    }

  /*  fun checkPhoneRQ(txt:String){
        if (BasicTools.isConnected(this@EnterPhoneNumberAcivity)) {



            showShimmerBtn(true)

            var map = HashMap<String, String>()
            map.put("email_phone",txt)
            val shopApi = ApiClient.getClientJwt(
                BasicTools.getToken(this@EnterPhoneNumberAcivity),  BasicTools.getProtocol(this@EnterPhoneNumberAcivity).toString())
                ?.create(AppApi::class.java)
            val observable= shopApi!!.checkPhone(map)
            disposable.clear()
            disposable.add(
                observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : AppObservable<MessageModel>(this@EnterPhoneNumberAcivity) {
                        override fun onSuccess(result: MessageModel) {


                            showShimmerBtn(false)

                            if(!result.status!!){

                                TemplateActivity.SignUpModel.phoneNumber=editphone.text.trim().toString()
                                TemplateActivity.SignUpModel.countryCode=countryCode.selectedCountryCodeWithPlus
                               // TemplateActivity.SignUpModel.smsCode=credential.smsCode.toString()
                                TemplateActivity.storedVerificationId=""
                                TemplateActivity.tokenResendOtp=null
                                BasicTools.openActivity(this@EnterPhoneNumberAcivity,EnterCodeActivity::class.java,false)



                            }else{
                                showToastMessage(R.string.check_phone_hint)
                            }


                        }
                        override fun onFailed(status: Int) {


                            TemplateActivity.SignUpModel.phoneNumber=editphone.text.trim().toString()
                            TemplateActivity.SignUpModel.countryCode=countryCode.selectedCountryCodeWithPlus
                            // TemplateActivity.SignUpModel.smsCode=credential.smsCode.toString()
                            TemplateActivity.storedVerificationId=""
                            TemplateActivity.tokenResendOtp=null
                            BasicTools.openActivity(this@EnterPhoneNumberAcivity,EnterCodeActivity::class.java,false)
                            showShimmerBtn(false)
                            showToastMessage(R.string.faild)
                            //BasicTools.logOut(parent!!)
                        }
                    }))

        }
        else {
            showShimmerBtn(false)
            showToastMessage(R.string.no_connection)}
    }*/


}
