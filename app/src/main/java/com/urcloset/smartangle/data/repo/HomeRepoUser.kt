package com.example.currencyapp.data.repo

import android.content.Context
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onSuccess
import com.urcloset.smartangle.R
import com.urcloset.smartangle.activity.publishStatusActivity.InterfacePublication
import com.urcloset.smartangle.activity.publishStatusActivity.PublicationStatus
import com.urcloset.smartangle.api.AppApi
import com.urcloset.smartangle.data.model.agreement_terms.AgreementResponseTerms
import com.urcloset.smartangle.model.ProductModel
import com.urcloset.smartangle.model.PublishStateModel
import com.urcloset.smartangle.tools.BasicTools
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import javax.inject.Inject
import kotlin.collections.HashMap


class HomeRepoUser @Inject constructor(private val webService: AppApi,
                                        @ApplicationContext val context: Context) {

    suspend fun postAgreeConditions( hashMap: HashMap<String,Int>,completion: (ResponseBody?, String?) -> Unit) {
        val res =  webService.postAgreements(hashMap)//webService.postIgnoreOrder(hashMap)

        res?.onSuccess {
            completion(data!! , null)


        }
        res?.onException {
            completion(null ,message.toString())


        }
        res?.onError {
            completion(null,context.getString(R.string.error_happend)) // handle error from error body
        }
    }

    suspend fun getAgreeCondition( completion: (AgreementResponseTerms?, String?) -> Unit) {
        val res =  webService.getAgreementText()//webService.postIgnoreOrder(hashMap)

        res?.onSuccess {
            completion(data!! , null)


        }
        res?.onException {
            completion(null ,message.toString())


        }
        res?.onError {
            completion(null,context.getString(R.string.error_happend)) // handle error from error body
        }
    }
    suspend fun getUnPaidProducts( completion: (List<ProductModel.Product>?, String?) -> Unit) {
        val res =  webService.getUnPaidProducts()//webService.postIgnoreOrder(hashMap)

        res?.onSuccess {
            completion(data!!.data , null)


        }
        res?.onException {
            completion(null ,message.toString())


        }
        res?.onError {
            completion(null,context.getString(R.string.error_happend)) // handle error from error body
        }
    }

    suspend fun postPaymentMethod( hashMap: HashMap<String, Any>?,completion: (ResponseBody?, String?) -> Unit) {
        val res =  webService.postPaymentAfterMyFatoraPay(hashMap)//webService.postIgnoreOrder(hashMap)

        res?.onSuccess {
            completion(data!! , null)


        }
        res?.onException {
            completion(null ,message.toString())


        }
        res?.onError {
            completion(null,context.getString(R.string.error_happend)) // handle error from error body
        }
    }

    suspend fun getProducts(interfacePublicationStatus: InterfacePublication, completion: (List<ProductModel.Product>?, String?) -> Unit) {
        val res = interfacePublicationStatus.getCurrentList(webService)//webService.postIgnoreOrder(hashMap)

        res.onSuccess {
            completion(data!!.data , null)


        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null,context.getString(R.string.error_happend)) // handle error from error body
        }
    }

    suspend fun changeProductStatus(hashMap: HashMap<String, Any>?, completion: (ResponseBody?, String?) -> Unit) {
        val res = webService.deleteProduct(hashMap)//webService.postIgnoreOrder(hashMap)

        res.onSuccess {
            completion(data!! , null)


        }
        res.onException {
            completion(null ,message.toString())


        }
        res.onError {
            completion(null,context.getString(R.string.error_happend)) // handle error from error body
        }
    }
}