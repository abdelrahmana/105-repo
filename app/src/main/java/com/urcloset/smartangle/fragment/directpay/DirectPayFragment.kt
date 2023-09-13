package com.urcloset.smartangle.fragment.directpay

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.databinding.DirectPayLayoutBinding
import com.urcloset.smartangle.fragment.paymentmethod.PaymentMethodFragment
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.GetObjectGson
import com.urcloset.smartangle.tools.TemplateFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class DirectPayFragment() : TemplateFragment() {

    lateinit var binding : DirectPayLayoutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun init_events() {
    }

    override fun init_fragment(savedInstanceState: Bundle?) {
    }

    override fun on_back_pressed(): Boolean {
    return  true
    }
    @Inject lateinit var progressDialog : Dialog
    @Inject lateinit var getGetObjectGson: GetObjectGson
  //  @Inject lateinit var util: Util
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       // model.setItemActionClicked(itemClicked,itemAskPay,itemChat,compeleteOrder,itemRate,itemSubscripers)
        binding = DirectPayLayoutBinding.inflate(layoutInflater,container,false)
       // checkCurrentFragmentInterface.setLiveData(model.getMutableLiveOnReview()) // set listener callback
     // set item clicked actions
     binding.toolbar.ivBack.setOnClickListener{
         requireActivity().onBackPressed()
     }
        binding.send.setOnClickListener{
            if (binding.priceEditText.text.isNotEmpty())
            sendSelectedListToPaymentPage(System.currentTimeMillis().toString(),binding.priceEditText.text
                .toString() )
        }
        return binding.root//inflater.inflate(R.layout.fragment_join_request_one_item, container, false)
    }

    private fun sendSelectedListToPaymentPage(transactionId: String, priceText: String) {
        val hashMap = HashMap<String,Any?>()
       // Log.v("selected_products",selectedProducts.size.toString())
      // val productsIds = selectedProducts.map { (it.id?.toString()) }
        hashMap[PaymentMethodFragment.IDS] = transactionId//productsIds
        hashMap[PaymentMethodFragment.SOLD_IDS] = transactionId//productsIds

     //   hashMap[PaymentMethodFragment.SELECTED_PRODUCTS] = selectedProducts

        hashMap[PaymentMethodFragment.AMOUNT] = /*(binding.valueCommission.text.toString())*/(priceText).toDouble()
        val bundle = bundleOf(PaymentMethodFragment.PAYMENTPUBLISH to Gson().toJson(hashMap))
     //   val bottomSheetSold = BottomSheetSold()
     //   bottomSheetSold.arguments = bundle
     /*   bottomSheetSold
            .show(requireActivity().supportFragmentManager, "sold_bottom_Sheet")
        BasicTools.changeFragmentBack(
            requireActivity(),
            PaymentMethodFragment(),
            "payment",
            bundle,
            R.id.root_fragment_home
        )*/ BasicTools.changeFragmentBack(
            requireActivity(),
            PaymentMethodFragment(),
            "payment_pay",
            bundle,
            R.id.root_fragment_home
        )

    }
    companion object {
        val STATICPERPRODUCTCOMMISSION = 10
    }

}
