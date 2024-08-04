package com.urcloset.smartangle.deeplink

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Suppress("RegExpRedundantEscape")
@Parcelize
enum class DeepLink(val deepLink: Regex) : Parcelable {
    PRODUCTDETAILS(Regex("^.*\\/#\\/product\\/(\\d+)\$")),
}