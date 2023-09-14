package com.urcloset.smartangle.fragment.historypayment

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
import com.urcloset.smartangle.data.model.paymenthistory.Data
import com.urcloset.smartangle.databinding.HistoryPaymentLayoutBinding
import com.urcloset.smartangle.databinding.ProductPublishStateLayoutBinding
import com.urcloset.smartangle.databinding.UnpaidCommissionLayoutBinding
import com.urcloset.smartangle.fragment.bottomsheetagree.ConsentBottomSheet
import com.urcloset.smartangle.fragment.bottomsheetagree.ImplementerRegisterConsent
import com.urcloset.smartangle.fragment.historypayment.adaptor.AdaptorOneItemHistory
import com.urcloset.smartangle.fragment.paymentmethod.PaymentMethodFragment
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.BasicTools
import com.urcloset.smartangle.tools.GetObjectGson
import com.urcloset.smartangle.tools.TemplateFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
@AndroidEntryPoint
class HistoryPaymentFragment() : TemplateFragment() {

     var binding : HistoryPaymentLayoutBinding? =null
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding =null
    }

    val model : HistoryViewModel by viewModels()
    @Inject lateinit var progressDialog : Dialog
  //  @Inject lateinit var util: Util
    lateinit var adapterOneItem : AdaptorOneItemHistory
    val arrayList = ArrayList<Data>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
       // model.setItemActionClicked(itemClicked,itemAskPay,itemChat,compeleteOrder,itemRate,itemSubscripers)
        binding = HistoryPaymentLayoutBinding.inflate(layoutInflater,container,false)
       // checkCurrentFragmentInterface.setLiveData(model.getMutableLiveOnReview()) // set listener callback
     // set item clicked actions
        binding?.header?.headerText?.text = getString(R.string.record_payment)
     binding?.header?.backImage?.setOnClickListener{
         requireActivity().onBackPressed()
     }
        adapterOneItem = AdaptorOneItemHistory(requireContext(),arrayList)
        BasicTools.setRecycleView(
            binding?.recycleHistoryPayment, adapterOneItem, LinearLayoutManager.VERTICAL,
            requireContext(), null, false
        )
        setViewModelObservers()
        return binding!!.root//inflater.inflate(R.layout.fragment_join_request_one_item, container, false)
    }

    fun setViewModelObservers() {
        model.getPaymentHistory(requireContext())
        model.getHistory.observe(viewLifecycleOwner,Observer{ // for sending to maintenance
            if (it!=null){
                if ((it.data?.size?:0)>0) {
                    binding?.noResult?.visibility=(View.GONE)
                    adapterOneItem.clearProduct() // for now delete old data
                    adapterOneItem.updateList(it.data?:ArrayList())
                } else
                    binding?.noResult?.visibility= (View.VISIBLE)
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
