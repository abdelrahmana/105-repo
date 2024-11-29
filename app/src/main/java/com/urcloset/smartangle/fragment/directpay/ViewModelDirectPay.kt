package com.urcloset.smartangle.fragment.directpay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ViewModelDirectPay : ViewModel() {
    val mutableStateCommission = MutableStateFlow<String?> (null)
    val stateCommission : StateFlow<String?> = mutableStateCommission

    fun getPrice(referenceConnection: FirebaseDatabase,callBackUnit :(String)->Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            valueEventListener?.let {it->
                referenceConnection.getReference().removeEventListener(it)
            }
            valueEventListener = object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val genericTypeIndicator: GenericTypeIndicator<Map<String?, Any?>?> =
                        object : GenericTypeIndicator<Map<String?, Any?>?>() {}
                    val info: Map<String?, Any?>? = snapshot.getValue(genericTypeIndicator)
                    val comissionString = (info?.get(("commission") ?: "10")?:"10") as String
                    viewModelScope.launch(Dispatchers.IO) {
                        mutableStateCommission.emit(comissionString)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            }
            referenceConnection.getReference().addValueEventListener(valueEventListener!!)

            }

        }
    var valueEventListener: ValueEventListener? = null

}

