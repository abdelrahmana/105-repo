package com.urcloset.smartangle.activity.searchActivity

import com.urcloset.smartangle.model.project_105.ColorModelHassan

interface ISearchActivity {

    fun setColorData(item:ArrayList<ColorModelHassan.Color>)


    fun checkIfAdapterEmpty(item:Boolean)
}