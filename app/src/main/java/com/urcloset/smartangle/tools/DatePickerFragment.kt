package com.urcloset.smartangle.tools

import androidx.fragment.app.DialogFragment


import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.annotation.NonNull
import java.util.*


class DatePickerFragment : DialogFragment() {


    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(activity!!,activity as DatePickerDialog.OnDateSetListener?,year,month,day)
       // dialog.window!!.attributes.windowAnimations = R.style.scaleAnimation

        return dialog
    }


}