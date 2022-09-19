package com.urcloset.smartangle.tools

import android.graphics.Bitmap

interface DownloadListener {
    fun completed(status: Boolean,bitmap: Bitmap)
}
