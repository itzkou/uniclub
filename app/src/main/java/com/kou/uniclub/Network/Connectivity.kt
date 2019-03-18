package com.kou.uniclub.Network


import android.content.Context
import android.net.ConnectivityManager



class Connectivity {

    companion object {
        fun isConnected(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val isConnected: Boolean
            val activeNetwork = connectivityManager.activeNetworkInfo
            isConnected = activeNetwork != null && activeNetwork.isConnected

            return isConnected
        }

    }

}