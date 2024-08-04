package com.urcloset.smartangle.globals

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

class ForceUpdateChecker constructor(
     val context: Context,
     val onUpdateNeededListener: ((String, Boolean) -> Unit)?
) {

    fun check() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.fetch(0).addOnSuccessListener {
            remoteConfig.activate().addOnSuccessListener {
                val currentVersion = remoteConfig.getString(KEY_CURRENT_VERSION)
                val appVersion = getAppVersion(context)
                val updateUrl = remoteConfig.getString(KEY_UPDATE_URL)
                val keyForce = remoteConfig.getBoolean(keyForceUpdate)
                if (currentVersion > appVersion) {
                    onUpdateNeededListener?.invoke(updateUrl, keyForce)
                }

            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getAppVersion(context: Context): String {
        var result = ""
        try {
            result =
                with(context.packageManager) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        getPackageInfo(
                            context.packageName,
                            PackageManager.PackageInfoFlags.of(0L)
                        ).versionName
                    } else {
                        getPackageInfo(context.packageName, 0).versionName
                    }
                }
            result = result.replace("[a-zA-Z]|-".toRegex(), "")
        } catch (e: PackageManager.NameNotFoundException) {
        }
        return result
    }


    companion object {
        const val KEY_UPDATE_REQUIRED = "force_update_required"
        const val KEY_CURRENT_VERSION = "android_google_version"
        const val keyForceUpdate = "force_update_required_android"
        const val KEY_UPDATE_URL =
            "https://play.google.com/store/apps/details?id=com.urcloset.smartangle"
    }
}