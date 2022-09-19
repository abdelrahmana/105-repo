package com.urcloset.smartangle.tools

import java.util.ArrayList

interface IImagesListener {

    fun moveTo(position: Int, albumNum: String, paths: ArrayList<String>)

}
