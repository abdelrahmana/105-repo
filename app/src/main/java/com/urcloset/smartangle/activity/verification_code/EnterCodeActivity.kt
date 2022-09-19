package com.urcloset.smartangle.activity.verification_code


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.cardview.widget.CardView
import com.urcloset.smartangle.R

import com.urcloset.smartangle.activity.signUp.SignUpAcitivty
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity
import com.google.android.gms.auth.api.credentials.Credentials
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.urcloset.smartangle.activity.signUp.CompleteDataAct

import kotlinx.android.synthetic.main.toolbar_backpress1.*
import java.util.concurrent.TimeUnit


class EnterCodeActivity : TemplateActivity() {

    private  val REQ_USER_CONSET=200
    private  val RESOLVE_HINT=7889
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
   // var smsBroadcastRecevider:SmsBrodcastReceiver?=null
    var auth: FirebaseAuth?=null

    lateinit var editCode:EditText
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken

    lateinit var btnNext:CardView
  //  var counterDown: CountDownTimer?=null
    override fun set_layout() {
        setContentView(R.layout.activity_v_code_phone)
    }

    override fun init_activity(savedInstanceState: Bundle?) {


    }


    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]


        Log.i("TEST_TEST",phoneNumber)
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber("+$phoneNumber") // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@EnterCodeActivity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }


    private fun reStartPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]
        Log.i("TEST_TEST",phoneNumber)
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber("+$phoneNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@EnterCodeActivity) // Activity (for callback binding)
            .setCallbacks(callbacks)
            .setForceResendingToken(TemplateActivity.tokenResendOtp!!)// OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
        // [END start_phone_auth]
    }










    override fun onStart() {
        super.onStart()

    }

    override fun onStop() {
        super.onStop()

    }
    override fun init_views() {



        editCode=findViewById(R.id.edit_code)
        btnNext=findViewById(R.id.btn_next)

    }


    @SuppressLint("SetTextI18n")
    override fun init_events() {

        auth = Firebase.auth
      //  tv_number.setText("${TemplateActivity.SignUpModel.countryCode}${TemplateActivity.SignUpModel.phoneNumber}")

    /*    card_resend.setOnClickListener {
            reStartPhoneNumberVerification("${TemplateActivity.SignUpModel.countryCode}${TemplateActivity.SignUpModel.phoneNumber}")
        }*/

//        counterDown=object : CountDownTimer(60000,1000){
//            override fun onFinish() {
//                tv_resend.setText(" 00:00 ")
//                btn_next.visibility= View.VISIBLE
//                card_resend.visibility= View.VISIBLE
//
//                /*     if(BasicTools.isConnected(parent!!))
//                     getverifyCodeRQ()
//                     else
//                         parent!!.showToastMessage(R.string.no_connection)*/
//
//            }
//
//            @SuppressLint("SetTextI18n")
//            override fun onTick(p0: Long) {
//
//                val seconds :Int= (p0 / 1000).toInt()
//                tv_resend.setText("00:"+  BasicTools.normalNumber(seconds.toString())+" ")
//            }
//
//        }.start()
//

        iv_back.setOnClickListener {
            TemplateActivity.SignUpModel.smsCode=""
            BasicTools.exitActivity(this@EnterCodeActivity)
        }




        btnNext.setOnClickListener {


            var code=editCode.text.toString()

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
                            showToastMessage("${e.localizedMessage}")

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

                    storedVerificationId = verificationId
                    resendToken = token



                }
            }



        Log.i("TEST_TEST","${TemplateActivity.SignUpModel.countryCode}${TemplateActivity.SignUpModel.phoneNumber}")
        startPhoneNumberVerification("${TemplateActivity.registerValueCode}${TemplateActivity.registerValue}")

    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this@EnterCodeActivity) { task ->
                if (task.isSuccessful) {

                    Log.d("TEST_TEST", "signInWithCredential:success")

//                    val user = task.result?.user


                    //counterDown!!.cancel()
                    BasicTools.openActivity(this@EnterCodeActivity,
                        CompleteDataAct::class.java,true)
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
