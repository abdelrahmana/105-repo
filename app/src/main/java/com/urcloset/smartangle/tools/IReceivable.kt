package com.urcloset.smartangle.tools

import android.os.Bundle


interface IReceivable {

    fun onReceive(bundle: Bundle)

    fun onReceive(`object`: Any)

}
