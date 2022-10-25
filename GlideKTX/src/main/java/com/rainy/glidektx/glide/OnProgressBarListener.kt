package com.rainy.glidektx.glide

/**
 * @author jiangshiyu
 * @date 2022/10/25
 */
//监听progressbar进度
interface OnProgressBarListener {

    val currentPercentage:Float

    fun onProgress(bytesRead: Long, expectedLength: Long)
}