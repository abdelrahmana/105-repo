package com.urcloset.smartangle.tools




import android.os.Bundle



class DataMessage(val _receiver: IReceivable) {

    val _extra: Bundle

    init {
        _extra = Bundle()
    }

}
