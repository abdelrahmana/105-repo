package com.urcloset.smartangle.fragment.unpaid

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.urcloset.smartangle.R
import com.urcloset.smartangle.adapter.PublishStateAdapter
import com.urcloset.smartangle.databinding.ProductPublishStateLayoutBinding
import com.urcloset.smartangle.databinding.UnpaidCommissionLayoutBinding
import com.urcloset.smartangle.fragment.bottomsheetagree.ConsentBottomSheet
import com.urcloset.smartangle.fragment.bottomsheetagree.ImplementerRegisterConsent
import com.urcloset.smartangle.fragment.paymentmethod.PaymentMethodFragment
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.GetObjectGson
import com.urcloset.smartangle.tools.TemplateFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class UnpaidCommissionsFragment() : TemplateFragment() {

    lateinit var binding : UnpaidCommissionLayoutBinding
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

    val model : UnPaidViewModel by viewModels()
    @Inject lateinit var progressDialog : Dialog
    @Inject lateinit var getGetObjectGson: GetObjectGson
  //  @Inject lateinit var util: Util
    lateinit var adapterOneItem : UnpaidAdaptorItems
    val arrayList = ArrayList<ProductModel.Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       // model.setItemActionClicked(itemClicked,itemAskPay,itemChat,compeleteOrder,itemRate,itemSubscripers)
        binding = UnpaidCommissionLayoutBinding.inflate(layoutInflater,container,false)
       // checkCurrentFragmentInterface.setLiveData(model.getMutableLiveOnReview()) // set listener callback
     // set item clicked actions
     binding.header.backImage.setOnClickListener{
         requireActivity().onBackPressed()
     }
        adapterOneItem = UnpaidAdaptorItems(requireContext(),arrayList,callBackUpdateListWithPosition)
        BasicTools.setRecycleView(
            binding.rvProducts, adapterOneItem, LinearLayoutManager.VERTICAL,
            requireContext(), null, false
        )
        binding.payButton.setOnClickListener{
            sendSelectedListToPaymentPage(arrayList.filter { it.selectedItem })
        }
        setViewModelObservers()
        return binding.root//inflater.inflate(R.layout.fragment_join_request_one_item, container, false)
    }

    private fun sendSelectedListToPaymentPage(selectedProducts: List<ProductModel.Product>) {
        val hashMap = HashMap<String,Any?>()
        Log.v("selected_products",selectedProducts.size.toString())
       val productsIds = selectedProducts.map { (it.id?.toString()) }
        hashMap[PaymentMethodFragment.IDS] = productsIds
        hashMap[PaymentMethodFragment.SOLD_IDS] = productsIds

     //   hashMap[PaymentMethodFragment.SELECTED_PRODUCTS] = selectedProducts

        hashMap[PaymentMethodFragment.AMOUNT] = /*(binding.valueCommission.text.toString())*/(selectedProducts.size*STATICPERPRODUCTCOMMISSION).toDouble()
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

    val callBackUpdateListWithPosition : (ArrayList<ProductModel.Product>)->Unit = {
        if (it.isEmpty())
            setCommissionUpdate(0,0,View.GONE)
        else
            setCommissionUpdate((it.size*STATICPERPRODUCTCOMMISSION),it.size,View.VISIBLE)

    }

    private fun setCommissionUpdate(totalCommission: Int,numberProducts : Int, visibilty: Int) {
        binding.valueNumber.text = numberProducts.toString()
        binding.valueCommission.text = (totalCommission.toString()) + " " + getString(R.string.sar)
        binding.payButton.visibility = visibilty
    }

    fun setViewModelObservers() {
        model.getUnPaidProducts()
        model.offersOnReviewLiveData.observe(viewLifecycleOwner,Observer{ // for sending to maintenance
            if (it!=null){
                if (it.isNotEmpty()) {

                    binding.lyEmpty.visibility=(View.GONE)

                    adapterOneItem.clearProduct() // for now delete old data
                    adapterOneItem.updateList(it)
                    binding.lengthProducts.text = getString(R.string.unpaid_products,arrayList.size)

                } else
                    binding.lyEmpty.visibility= (View.VISIBLE)

            }

        })
     /*   model.actionOrderLiveData.observe(requireActivity(), Observer{
            it?.let { pair->
                callBackUpdateListWithPosition.invoke(pair.first) // remove item from list
                BasicTools.showSnackMessages(requireActivity(),pair.second, R.color.green)
            }
        })*/

        model.networkLoader.observe(viewLifecycleOwner, Observer{
            it?.let { progress->
                progress.setDialog(progressDialog) // open close principles
                model.setNetworkLoader(null)
            }
        })

        model.errorViewModel.observe(viewLifecycleOwner) {
            it?.let { error ->
                BasicTools.showSnackMessages(requireActivity(), error)

            }
        }
    }
    companion object {
        val STATICPERPRODUCTCOMMISSION = 10
    }

}
