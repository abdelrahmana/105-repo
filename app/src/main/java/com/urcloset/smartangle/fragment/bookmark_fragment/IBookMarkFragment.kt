package com.urcloset.smartangle.fragment.bookmark_fragment

interface IBookMarkFragment {

    fun delete(id:Int,pos:Int)

    fun checkIfAdapterEmpty(item:Boolean)
}