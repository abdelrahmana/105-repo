package com.urcloset.smartangle.activity.forgotPassword


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import com.urcloset.smartangle.R
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_enter_code.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_backpress1.*
import java.util.concurrent.TimeUnit

class EnterCodeForgotPassowrdPhoneActivity : TemplateActivity() {
    var auth: FirebaseAuth?=null
    var counterDown: CountDownTimer?=null
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    override fun set_layout() {
        setContentView(R.layout.activity_enter_code)
    }

    override fun init_activity(savedInstanceState: Bundle?) {

    }


    override fun init_views() {



    }


    @SuppressLint("SetTextI18n")
    override fun init_events() {

        auth = Firebase.auth
        tv_number.setText("${TemplateActivity.countryCodeForgotPasswrod}${TemplateActivity.phoneForgotPasswrod}")

        card_resend.setOnClickListener {
            reStartPhoneNumberVerification("${TemplateActivity.countryCodeForgotPasswrod}${TemplateActivity.phoneForgotPasswrod}")

        }

        counterDown=object : CountDownTimer(60000,1000){
            override fun onFinish() {
                tv_resend.setText(" 00:00 ")
                btn_next.visibility= View.VISIBLE
                card_resend.visibility= View.VISIBLE

                /*     if(BasicTools.isConnected(parent!!))
                     getverifyCodeRQ()
                     else
                         parent!!.showToastMessage(R.string.no_connection)*/

            }

            @SuppressLint("SetTextI18n")
            override fun onTick(p0: Long) {

                val seconds :Int= (p0 / 1000).toInt()
                tv_resend.setText("00:"+  BasicTools.normalNumber(seconds.toString())+" ")
            }

        }.start()


        iv_back.setOnClickListener {
            TemplateActivity.SignUpModel.smsCode=""
            BasicTools.exitActivity(this@EnterCodeForgotPassowrdPhoneActivity)
        }
    /*    btn_next.setOnClickListener {

            showToastMessage(TemplateActivity.SignUpModel.smsCode.length.toString())
            var code=edit_1.text.toString().trim()+edit_2.text.toString().trim()+edit_3.text.toString().trim()+edit_4.text.toString().trim()+edit_5.text.toString().trim()+edit_6.text.toString().trim()

            if(TemplateActivity.SignUpModel.smsCode.isNotEmpty()) {

                counterDown!!.cancel()
                  BasicTools.openActivity(this@EnterCodeForgotPassowrdPhoneActivity,ForgotPassowrdActivity::class.java,true)
            }

            else if(TemplateActivity.SignUpModel.smsCode.equals(code)&&code.isNotEmpty()){
                counterDown!!.cancel()

                BasicTools.openActivity(this@EnterCodeForgotPassowrdPhoneActivity,ForgotPassowrdActivity::class.java,true)
            }
            else if(!TemplateActivity.SignUpModel.smsCode.equals(code)){

                showToastMessage(R.string.enter_code_wrong_code)
            }

            else {

            }

        }*/

        btn_next.setOnClickListener {


            var code=edit_code.text.toString()

            if(code.isNullOrEmpty()){
                showToastMessage(R.string.enter_code)
                return@setOnClickListener  }

            Log.i("TEST_TEST","code $code / id ${TemplateActivity.storedVerificationId}")
            val credential = PhoneAuthProvider.getCredential(TemplateActivity.storedVerificationId!!, code)

            signInWithPhoneAuthCredential(credential)
        }

        callbacks =
            object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    Log.d("TEST_TEST", "onVerificationCompleted:$credential")
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    Log.w("TEST_TEST", "onVerificationFailed", e)

                    when (e) {
                        is FirebaseAuthInvalidCredentialsException -> {
                            // Invalid request
                            showToastMessage("Invalid request")

                        }
                        is FirebaseTooManyRequestsException -> {
                            // The SMS quota for the project has been exceeded
                            showToastMessage("the SMS quota for the project has been exceeded")

                        }
                        else -> {
                            Log.e("TEST","error_here")
                            showToastMessage("error_here")

                        }
                    }


                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {

                    Log.d("TEST_TEST", "onCodeSent:$verificationId")
                    TemplateActivity.storedVerificationId=verificationId
                    TemplateActivity.tokenResendOtp= token
                    // Save verification ID and resending token so we can use them later
                    storedVerificationId = verificationId
                    resendToken = token

                    // counterDown!!.cancel()


                    //  val credential = PhoneAuthProvider.getCredential(verificationId!!, code)

                    //  BasicTools.openActivity(this@EnterCodeActivity,SignUpAcitivty::class.java,true)

                }
            }



        startPhoneNumberVerification("${TemplateActivity.countryCodeForgotPasswrod}${TemplateActivity.phoneForgotPasswrod}")


    }


    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@EnterCodeForgotPassowrdPhoneActivity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }


    private fun reStartPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@EnterCodeForgotPassowrdPhoneActivity) // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(TemplateActivity.tokenResendOtp!!)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }


    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this@EnterCodeForgotPassowrdPhoneActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TEST_TEST", "signInWithCredential:success")

//                    val user = task.result?.user


                    counterDown!!.cancel()
                    BasicTools.openActivity(this@EnterCodeForgotPassowrdPhoneActivity,ForgotPassowrdActivity::class.java,true)
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w("TEST_TEST", "signInWithCredential:failure", task.exception)
                    showToastMessage("Sign in failed")

                }
            }
    }

    override fun set_fragment_place() {

    }


}
