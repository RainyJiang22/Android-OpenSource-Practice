package com.rainy.glidektx.glide

import okhttp3.HttpUrl

/**
 * @author jiangshiyu
 * @date 2022/10/25
 *
 */
//通知正在监听该url下载进度的操作
interface ResponseProgressListener {

    fun update(url: HttpUrl, byteRead: Long, contentLength: Long)

}