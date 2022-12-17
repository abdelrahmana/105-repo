package com.urcloset.smartangle.fragment.bottomsheetagree

interface WhichLayer {
    fun requireApiCall():Boolean
    fun shortDescription(): Boolean
}

class  ImplementerRegisterConsent :WhichLayer {
    override fun requireApiCall(): Boolean {
        return false
    }

    override fun shortDescription(): Boolean {
        return false
    }

}
class  ImplementerPublishConsent :WhichLayer {
    override fun requireApiCall(): Boolean {
        return true
    }

    override fun shortDescription(): Boolean {
        return true
    }

}