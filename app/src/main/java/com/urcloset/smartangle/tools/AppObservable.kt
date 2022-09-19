package com.urcloset.smartangle.tools



import android.content.Context
import android.widget.Toast
import com.urcloset.smartangle.R


import java.net.SocketTimeoutException
import io.reactivex.annotations.NonNull
import io.reactivex.observers.DisposableObserver
import retrofit2.HttpException



abstract class AppObservable<T> : DisposableObserver<T> {

    private var context: Context? = null
    private var silent = false

    constructor(context: Context) {
        this.context = context
    }

    constructor(context: Context, silent: Boolean) {
        this.context = context
        this.silent = silent
    }

    open abstract fun onSuccess(result: T)
    open fun onFailed(status: Int) {

    }


    override fun onNext(@NonNull t: T) {
        onSuccess(t)
    }

    override fun onComplete() {

    }

    override fun onError(@NonNull e: Throwable) {
        if (!silent) {
            if (e is SocketTimeoutException) {
                Toast.makeText(context, context!!.getString(R.string.timeout), Toast.LENGTH_SHORT).show()

            }
        }
        if (e is HttpException) {
            val statsuCode = e.code()
            onFailed(statsuCode)
        } else {
            onFailed(0)
        }
        e.printStackTrace()
    }
}
