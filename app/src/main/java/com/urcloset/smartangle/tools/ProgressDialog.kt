package com.urcloset.smartangle.tools

import android.app.Dialog

interface ProgressDialog {
    fun setDialog(progressDialog : Dialog)

}
class ShowProgress():ProgressDialog {
    override fun setDialog(progressDialog: Dialog) {
        progressDialog.show()
    }

}
class DissMissProgress():ProgressDialog {
    override fun setDialog(progressDialog: Dialog) {
        progressDialog.dismiss()
    }

}