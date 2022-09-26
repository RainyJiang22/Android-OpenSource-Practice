package com.rainy.livedatahttp.util

import android.content.Context
import android.net.ConnectivityManager
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author jiangshiyu
 * @date 2022/9/23
 */
object NetWorkUtil {


    /**
     * check NetWorkAvailable
     *
     * you must to be request the permission ACCESS_NETWORK_STATE
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return null != info && info.isAvailable
    }


    /**
     * ping network
     */
    private fun connectionNetwork(): Boolean {
        var result = false
        var httpUrl: HttpURLConnection? = null
        try {
            httpUrl = URL("http://www.baidu.com")
                .openConnection() as HttpURLConnection
            httpUrl.connectTimeout = 3000
            httpUrl.connect()
            result = true
        } catch (e: IOException) {
        } finally {
            httpUrl?.disconnect()
            httpUrl = null
        }
        return result
    }


}