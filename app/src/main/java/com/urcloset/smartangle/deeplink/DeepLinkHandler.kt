package com.urcloset.smartangle.deeplink

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.os.bundleOf
import com.urcloset.smartangle.activity.productDetails.ProductDetails

interface DeepLinkHandler {

    fun handleDeepLink(uri: Uri?)

    class Base(
        private val context: Context,
    ) : DeepLinkHandler {
        override fun handleDeepLink(uri: Uri?) {
            uri?.toString()?.let { url ->
                    when (parseDeepLink(url)) {
                        DeepLink.PRODUCTDETAILS -> {
                            handleOrderDetails(url)
                        }
                        else -> return@let
                    }
                  /*  try {
                    } catch (e: PendingIntent.CanceledException) {
                        e.printStackTrace()
                    }*/

            }
        }

        private fun handleOrderDetails(url: String) {
            url.substringAfterLast("/").toIntOrNull()?.let { productId ->
                context.startActivity(Intent(context,ProductDetails::class.java).putExtra("id",productId.toString()))

            }
        }

        private fun parseDeepLink(deepLink: String): DeepLink? {
            return DeepLink.values().find {
                deepLink.matches(it.deepLink)
            }
        }

        companion object {
        }
    }
}