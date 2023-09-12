package com.urcloset.smartangle.activity.notificationActivity

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.data.repo.HomeRepoUser
import com.urcloset.smartangle.base.BaseViewModels
import com.urcloset.smartangle.model.BasicModel
import com.urcloset.smartangle.tools.DissMissProgress
import com.urcloset.smartangle.tools.ShowProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class NotificationViewModel @Inject constructor(val repoDi : HomeRepoUser) : BaseViewModels() {
    private val _deleteAllNotifications = MutableLiveData<BasicModel?>()
    val deleteAllNotifications :LiveData<BasicModel?> = _deleteAllNotifications

    //  private val offersOnComplete = MutableLiveData<ArrayList<Content>?>()
   // val offersOnCompelete :LiveData<ArrayList<Content>?> = offersOnComplete //fun getMutableLiveOnTheWay() = offersOnTheWay
    fun setDeleteProduct(deleteNotifications : BasicModel?) {
        _deleteAllNotifications.postValue(deleteNotifications)
    }
    fun deleteAllNotifications(
        hashMap: HashMap<String, Any>? // to access Order List By Status, productPosition: kotlin.Int){}, productPosition: kotlin.Int){}
    ) {
        setNetworkLoader(ShowProgress())
        viewModelScope.launch {
            /*  orderListByStatus.getCurrentList(repoDi,hashMap
              )*/
            repoDi.deleteAllNotifications(hashMap){ response, errors ->
                setNetworkLoader(DissMissProgress())

                response?.let { it ->
                    // offersOnTheWay.value = it as ArrayList
                    //  orderListByStatus.getLiveData().value = it as ArrayList
                    _deleteAllNotifications.value = response
                }
                errors?.let { it ->
                    setError(it)
                }

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