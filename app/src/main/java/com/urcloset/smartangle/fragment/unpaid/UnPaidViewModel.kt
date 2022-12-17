package com.urcloset.smartangle.fragment.unpaid

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.data.repo.HomeRepoUser
import com.urcloset.smartangle.activity.publishStatusActivity.InterfacePublication
import com.urcloset.smartangle.base.BaseViewModels
import com.urcloset.smartangle.di.RepoDi
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.DissMissProgress
import com.urcloset.smartangle.tools.ProgressDialog
import com.urcloset.smartangle.tools.ShowProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import droidninja.filepicker.viewmodels.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class UnPaidViewModel @Inject constructor(val repoDi : HomeRepoUser) : BaseViewModels() {

    private val offersOnReview = MutableLiveData<ArrayList<ProductModel.Product>?>()
    val offersOnReviewLiveData :LiveData<ArrayList<ProductModel.Product>?> = offersOnReview

    fun getUnPaidProducts() {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
          /*  orderListByStatus.getCurrentList(repoDi,hashMap
            )*/
            repoDi.getUnPaidProducts(){ response, errors ->
                response?.let { it ->
                   // offersOnTheWay.value = it as ArrayList
                  //  orderListByStatus.getLiveData().value = it as ArrayList
                    offersOnReview.value = response as ArrayList<ProductModel.Product>?
                }
                errors?.let { it ->
                    setError(it)
                }
                setNetworkLoader(DissMissProgress())

            }

        }
    }
    fun clearLiveDataOnFragment(removeOnFragment: Fragment){
        // errorViewModel.removeObservers(removeOnActivity)
        setError(null)
        networkLoader.removeObservers(removeOnFragment)
        setNetworkLoader(null)


    }

}