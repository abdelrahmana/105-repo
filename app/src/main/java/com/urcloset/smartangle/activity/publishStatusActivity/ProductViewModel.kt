package com.urcloset.smartangle.activity.publishStatusActivity

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.data.repo.HomeRepoUser
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.tools.DissMissProgress
import com.urcloset.smartangle.tools.ProgressDialog
import com.urcloset.smartangle.tools.ShowProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
open class ProductViewModel @Inject constructor(val repoDi : HomeRepoUser) : ViewModel() {

    private val offersOnReview = MutableLiveData<ArrayList<ProductModel.Product>?>()
    val offersOnReviewLiveData :LiveData<ArrayList<ProductModel.Product>?> = offersOnReview

    private val _deleteProudct = MutableLiveData<Pair<Int,ResponseBody>?>()
    val deleteProudct :LiveData<Pair<Int,ResponseBody>?> = _deleteProudct

    //  private val offersOnComplete = MutableLiveData<ArrayList<Content>?>()
   // val offersOnCompelete :LiveData<ArrayList<Content>?> = offersOnComplete
    private val _errorViewModel = MutableLiveData<String?>()
    val errorViewModel :LiveData<String?> = _errorViewModel
    private val _networkLoader = MutableLiveData<ProgressDialog?>()
    val networkLoader :LiveData<ProgressDialog?> = _networkLoader
    fun getMutableLiveOnReview() = offersOnReview
    //fun getMutableLiveOnTheWay() = offersOnTheWay
    fun setDeleteProduct(deleteProudct : Pair<Int,ResponseBody>?) {
        _deleteProudct.postValue(deleteProudct)
    }
    fun setNetworkLoader(loader : ProgressDialog?) {
        _networkLoader.postValue(loader)
    }
    fun setError(error : String?) {
        _errorViewModel.postValue(error)
    }
    fun changeProductStatus(
        hashMap: HashMap<String, Any>?,productPosition : Int // to access Order List By Status, productPosition: kotlin.Int){}, productPosition: kotlin.Int){}
    ) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
            /*  orderListByStatus.getCurrentList(repoDi,hashMap
              )*/
            repoDi.changeProductStatus(hashMap){ response, errors ->
                setNetworkLoader(DissMissProgress())

                response?.let { it ->
                    // offersOnTheWay.value = it as ArrayList
                    //  orderListByStatus.getLiveData().value = it as ArrayList
                    _deleteProudct.value = Pair(productPosition,response)
                }
                errors?.let { it ->
                    setError(it)
                }

            }

        }
    }
    fun getOffersDependOnStatus(
        orderListByStatus: InterfacePublication // to access Order List By Status
    ) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
          /*  orderListByStatus.getCurrentList(repoDi,hashMap
            )*/
            repoDi.getProducts(orderListByStatus){ response, errors ->
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