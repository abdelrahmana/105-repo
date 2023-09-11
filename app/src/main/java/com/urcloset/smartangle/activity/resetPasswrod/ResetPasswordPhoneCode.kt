package com.urcloset.smartangle.activity.resetPasswrod


import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.signUp.CompleteDataAct
import com.urcloset.smartangle.databinding.ActivityVCodePhoneBinding
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.TemplateActivity

import java.util.concurrent.TimeUnit

class ResetPasswordPhoneCode : TemplateActivity() {
  
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    var auth: FirebaseAuth?=null
    lateinit var editCode: EditText
    lateinit var ivBack: ImageView
    private var storedVerificationId: String? = ""
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    lateinit var btnNext: CardView
    var binding : ActivityVCodePhoneBinding?=null
    override fun set_layout() {
        binding = ActivityVCodePhoneBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
    }

    override fun init_activity(savedInstanceState: Bundle?) {


    }


    private fun startPhoneNumberVerification(phoneNumber: String) {
        // [START start_phone_auth]


        Log.i("TEST_TEST",phoneNumber)
        val options = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber("+$phoneNumber") // Phone number to verify
            .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this@ResetPasswordPhoneCode) // Activity (for callback binding)
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
            .setActivity(this@ResetPasswordPhoneCode) // Activity (for callback binding)
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


        binding!!.toolbar.ivBack.setOnClickListener {
            TemplateActivity.SignUpModel.smsCode=""
            BasicTools.exitActivity(this@ResetPasswordPhoneCode)
        }




        btnNext.setOnClickListener {


            var code=editCode.text.toString()
            TemplateActivity.codeForEmailOrPhone=code
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
            .addOnCompleteListener(this@ResetPasswordPhoneCode) { task ->
                if (task.isSuccessful) {

                    Log.d("TEST_TEST", "signInWithCredential:success")

//                    val user = task.result?.user


                    //counterDown!!.cancel()
                    BasicTools.openActivity(this@ResetPasswordPhoneCode,
                        ResetPasswordNewPass::class.java,true)


                } else {

                    Log.w("TEST_TEST", "signInWithCredential:failure", task.exception)
                    showToastMessage("Sign in failed")

                }
            }
    }

    override fun set_fragment_place() {

    }


}
