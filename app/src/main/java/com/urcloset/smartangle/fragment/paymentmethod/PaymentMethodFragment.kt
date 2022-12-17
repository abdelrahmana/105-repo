package com.urcloset.smartangle.fragment.paymentmethod

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.myfatoorah.sdk.entity.executepayment.MFExecutePaymentRequest
import com.myfatoorah.sdk.entity.initiatepayment.MFInitiatePaymentRequest
import com.myfatoorah.sdk.entity.initiatepayment.MFInitiatePaymentResponse
import com.myfatoorah.sdk.entity.paymentstatus.MFGetPaymentStatusResponse
import com.myfatoorah.sdk.utils.MFAPILanguage
import com.myfatoorah.sdk.utils.MFCountry
import com.myfatoorah.sdk.utils.MFCurrencyISO
import com.myfatoorah.sdk.utils.MFEnvironment
import com.myfatoorah.sdk.views.MFResult
import com.myfatoorah.sdk.views.MFSDK
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.homeActivity.HomeActivity
import com.urcloset.smartangle.activity.homeActivity.HomeViewModel
import com.urcloset.smartangle.databinding.FragmentPaymentMethodBinding
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.GetObjectGson
import com.urcloset.smartangle.tools.TemplateActivity.Companion.loginResponse
import com.urcloset.smartangle.tools.TemplateFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PaymentMethodFragment : TemplateFragment() {
    lateinit var binding : FragmentPaymentMethodBinding
    val viewModel :PaymentViewModel by viewModels()
    @Inject lateinit var getGsonObject  :GetObjectGson
    @Inject lateinit var progressDialog : Dialog
    var selectedPayment : HashMap<String,Any>?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MFSDK.init(TOKENLIVE, MFCountry.SAUDI_ARABIA, MFEnvironment.LIVE)
        BasicTools.setLanguagePerActivity(requireActivity(),null)
      selectedPayment = getGsonObject.getGenericMap(arguments?.getString(PAYMENTPUBLISH)?:"")
    }

    override fun init_events() {

    }

    override fun init_fragment(savedInstanceState: Bundle?) {
    }

    override fun on_back_pressed(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentMethodBinding.inflate(layoutInflater)
        binding.progressLoader.visibility = View.VISIBLE
        MFSDK.initiateSession {
            when (it) {
                is MFResult.Success -> {
                    binding.mfPaymentView.load(it.response)
                }
                is MFResult.Fail -> {
                    Toast.makeText(requireContext(), Gson().toJson(it.error),Toast.LENGTH_SHORT).show()
                }
            }
            binding.progressLoader.visibility = View.GONE

        }
        binding.payNow.setOnClickListener{
            payButton((selectedPayment?.get(AMOUNT)?:"0.0").toString().toDouble())
          /*  viewModel.postPaymentAfterPay(selectedPayment.also {
                it?.put(PAYMENTID, 1902501)
                it?.put(PAYMENT_STATUS, "success")
                it?.put(RESPONSEOBJECT,"{\"CreatedDate\":\"2022-12-11T19:02:43.17\",\"CustomerMobile\":\"+965\",\"CustomerName\":\"badr\",\"ExpiryDate\":\"December 14, 2022\",\"InvoiceDisplayValue\":\"10.000 KD\",\"InvoiceId\":1902501,\"InvoiceItems\":[],\"InvoiceReference\":\"2022341740\",\"InvoiceStatus\":\"Paid\",\"InvoiceTransactions\":[{\"AuthorizationId\":\"024176\",\"Currency\":\"KD\",\"CustomerServiceCharge\":\"0.000\",\"DueValue\":\"10.000\",\"PaidCurrency\":\"KD\",\"PaidCurrencyValue\":\"10.000\",\"PaymentGateway\":\"VISA/MASTER\",\"PaymentId\":\"07071902501137592271\",\"ReferenceId\":\"234516298477\",\"TrackId\":\"11-12-2022_1375922\",\"TransactionDate\":\"2022-12-11T19:02:54.22\",\"TransactionId\":\"298477\",\"TransactionStatus\":\"Succss\",\"TransationValue\":\"10.000\"}],\"InvoiceValue\":10.0,\"Suppliers\":[]}")
                it?.put(INVOICEID,1902501)
            })*/
        }
     /*   val request = MFInitiatePaymentRequest(0.100, MFCurrencyISO.SAUDI_ARABIA_SAR)
        MFSDK.initiatePayment(
            request,
           BasicTools.getLangCode(requireContext())?:"en"
        ) { result: MFResult<MFInitiatePaymentResponse> ->
            when (result) {
                is MFResult.Success ->
                    binding.mfPaymentView.load(result.response )

                is MFResult.Fail ->
                    Log.d("TAG", "Fail: " + Gson().toJson(result.error))
            }
        }*/
        setViewModelObservers()
        return binding.root
    }
    fun payButton(amount : Double){
        val request = MFExecutePaymentRequest(amount)
        request.customerName =BasicTools.getUserName(requireContext()) + " id  :" + (loginResponse?.data?.user?.id?:0).toString()
        binding.mfPaymentView.pay(
            requireActivity(),
            request,
            MFAPILanguage.EN,
            onInvoiceCreated = {
                Log.d("", "invoiceId: $it")
            }
        ) { invoiceId: String, result: MFResult<MFGetPaymentStatusResponse> ->
            when (result) {
                is MFResult.Success -> {
                    result.response.let {resultPay->
                        viewModel.postPaymentAfterPay(selectedPayment.also {
                            it?.put(PAYMENTID, resultPay.invoiceId?:0)
                            it?.put(PAYMENT_STATUS, "success")
                            it?.put(RESPONSEOBJECT,Gson().toJson(resultPay))
                            it?.put(INVOICEID,resultPay.invoiceId?:0)
                        })
                    }
                    Log.d("TAG", "Response: " + Gson().toJson(result.response))
                }
                is MFResult.Fail -> {
                    Toast.makeText(requireContext(),result.error.message?:"",Toast.LENGTH_SHORT).show()
                 /*   viewModel.postPaymentAfterPay(selectedPayment.also {
                        it?.put(PAYMENTID, invoiceId?:0)
                        it?.put(PAYMENT_STATUS, "error")
                        it?.put(RESPONSEOBJECT,result)
                        it?.put(INVOICEID,invoiceId?:0)
                        it?.put(ERRORMESSAGE,result.error.message?:"")

                    })*/
                    Log.d("TAG", "Fail: " + Gson().toJson(result.error))
                }
            }
            Log.d("TAG", "invoiceId: $invoiceId")
        }
    }
    fun setViewModelObservers() {
        viewModel.postPayment.observe(viewLifecycleOwner, Observer{ // for sending to maintenance
            if (it!=null){
                Toast.makeText(requireContext(),getString(R.string.process_completed),Toast.LENGTH_SHORT).show()
                requireActivity().startActivity(Intent(requireContext(),HomeActivity::class.java))
                requireActivity().finish()
            }

        })

        viewModel.networkLoader.observe(viewLifecycleOwner, Observer{
            it?.let { progress->
                progress.setDialog(progressDialog) // open close principles
                viewModel.setNetworkLoader(null)
            }
        })

        viewModel.errorViewModel.observe(viewLifecycleOwner) {
            it?.let { error ->
                BasicTools.showSnackMessages(requireActivity(), error)

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    companion object {
        const val API_KEY = "rLtt6JWvbUHDDhsZnfpAhpYk4dxYDQkbcPTyGaKp2TYqQgG7FGZ5Th_WD53Oq8Ebz6A53njUoo1w3pjU1D4vs_ZMqFiz_j0urb_BH9Oq9VZoKFoJEDAbRZepGcQanImyYrry7Kt6MnMdgfG5jn4HngWoRdKduNNyP4kzcp3mRv7x00ahkm9LAK7ZRieg7k1PDAnBIOG3EyVSJ5kK4WLMvYr7sCwHbHcu4A5WwelxYK0GMJy37bNAarSJDFQsJ2ZvJjvMDmfWwDVFEVe_5tOomfVNt6bOg9mexbGjMrnHBnKnZR1vQbBtQieDlQepzTZMuQrSuKn-t5XZM7V6fCW7oP-uXGX-sMOajeX65JOf6XVpk29DP6ro8WTAflCDANC193yof8-f5_EYY-3hXhJj7RBXmizDpneEQDSaSz5sFk0sV5qPcARJ9zGG73vuGFyenjPPmtDtXtpx35A-BVcOSBYVIWe9kndG3nclfefjKEuZ3m4jL9Gg1h2JBvmXSMYiZtp9MR5I6pvbvylU_PP5xJFSjVTIz7IQSjcVGO41npnwIxRXNRxFOdIUHn0tjQ-7LwvEcTXyPsHXcMD8WtgBh-wxR8aKX7WPSsT1O8d8reb2aR7K3rkV3K82K_0OgawImEpwSvp9MNKynEAJQS6ZHe_J_l77652xwPNxMRTMASk1ZsJL"
        const val TOKENLIVE = "IK475p34XGzhN1c3PqFqhY_gPCwwtvp9DsAdHsb1dJnyXleoT0Td-NTHJ-LJouepX_E6xgbF2-F_mopo9u6fywFw5v0GMon8mRhSdLyL0E_vh1yvI222z-OjbsS2EdU-Z-ih9d6gU7MnIxjc1aXt-IPnh62akkQV8pGlJelpmB8RMfAK2HmUrbby4YXwNIQb1bGCA7EdDJ6LeasCFFCasV-a_bYcwX9YuixkqxZGC3QFjDPBN7cAQ2p87W-2mpO6XYqeX3OG1AMfSWAIOXiiye96mIJTLn4Kgh022ZiJAhDtCUrFRwclUd1IxgxTXl9Cz-V21KJOQaVgFZ_42dOWk2vldS33vt9ombnyRdLDW-2PTVdT2nMYBykFiu5PrMmyATVJuqBpfpDnYMqhDDNcwOMDPa_KUWWf0Ppmn2yHgjZFdD4HR0ec--JlELMqBASq_QzXg2-dPbdAPeTRKMZmEO41Cl5KqYFV-GpvSMopu4twcXXniajeDzcyPHrvXcCRTGI6StALKhmwzNYwpx3oz2fXgWGjGVjOmlbATVIQdV4FB8BHikjiX7LjVCKwg8Mh1JLQkjPoV7JOsiF5kofNLRQsFhc9ImiZFIcuHreDuvGQZkGBXnDvJOSyB8s5LvqNuPz2KQtznjG5gCWYFe0Fg2SzE6-MhmEnIpDg-Bxe8f550t1Fi792g8M4jKf1W8LmPobpHlSKcaTMKD4UJig6E2zw1p0"
        const val TESTTOKEN = "rLtt6JWvbUHDDhsZnfpAhpYk4dxYDQkbcPTyGaKp2TYqQgG7FGZ5Th_WD53Oq8Ebz6A53njUoo1w3pjU1D4vs_ZMqFiz_j0urb_BH9Oq9VZoKFoJEDAbRZepGcQanImyYrry7Kt6MnMdgfG5jn4HngWoRdKduNNyP4kzcp3mRv7x00ahkm9LAK7ZRieg7k1PDAnBIOG3EyVSJ5kK4WLMvYr7sCwHbHcu4A5WwelxYK0GMJy37bNAarSJDFQsJ2ZvJjvMDmfWwDVFEVe_5tOomfVNt6bOg9mexbGjMrnHBnKnZR1vQbBtQieDlQepzTZMuQrSuKn-t5XZM7V6fCW7oP-uXGX-sMOajeX65JOf6XVpk29DP6ro8WTAflCDANC193yof8-f5_EYY-3hXhJj7RBXmizDpneEQDSaSz5sFk0sV5qPcARJ9zGG73vuGFyenjPPmtDtXtpx35A-BVcOSBYVIWe9kndG3nclfefjKEuZ3m4jL9Gg1h2JBvmXSMYiZtp9MR5I6pvbvylU_PP5xJFSjVTIz7IQSjcVGO41npnwIxRXNRxFOdIUHn0tjQ-7LwvEcTXyPsHXcMD8WtgBh-wxR8aKX7WPSsT1O8d8reb2aR7K3rkV3K82K_0OgawImEpwSvp9MNKynEAJQS6ZHe_J_l77652xwPNxMRTMASk1ZsJL"
        val IDS = "ids"
        val SELECTED_PRODUCTS = "SELECTED_PRODUCTS"
     val PAYMENTID = "payment_id"
        const val SOLD_IDS = "sold_ids"
     val PAYMENT_STATUS = "payment_status"
     val AMOUNT = "amount"
     val RESPONSEOBJECT = "response_object"
     val INVOICEID = "invoice_id"
     val ERRORMESSAGE = "error_message"
     val PAYMENTPUBLISH  = "PAYMENT_PUBLISH"
    }
}