package com.oliver.gymondo.network.connectivity

import android.content.Context
import android.net.ConnectivityManager

object ConnectivityUtils {
    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}