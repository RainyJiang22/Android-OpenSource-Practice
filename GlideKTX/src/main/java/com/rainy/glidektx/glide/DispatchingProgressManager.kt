package com.rainy.glidektx.glide

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.HttpUrl

/**
 * @author jiangshiyu
 * @date 2022/10/25
 */
class DispatchingProgressManager internal constructor() : ResponseProgressListener {

    companion object {

        //显示当前url的进度
        private val PROGRESSES = HashMap<String?, Long>()
        //存储UI监听器
        private val LISTENERS = HashMap<String?, OnProgressBarListener>()

        //将url和进度监听器存入到hashmap中
        internal fun expect(url: String?, listener: OnProgressBarListener) {
            LISTENERS[url] = listener
        }

        //如果下载完成/下载失败,移除url
        internal fun forget(url: String?) {
            LISTENERS.remove(url)
            PROGRESSES.remove(url)
        }
    }

    //后台线程通知进度，ui线程处理UI
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun update(url: HttpUrl, byteRead: Long, contentLength: Long) {
        val key = url.toString()

        //如果没有进度监听器，直接显示照片
        val listener = LISTENERS[key] ?: return
        if (contentLength <= byteRead) {
            forget(key)
        }
        if (needsDispatch(key, byteRead, contentLength, listener.currentPercentage)) { //8
            Log.d("glide", "update: $contentLength")
            handler.post { listener.onProgress(byteRead, contentLength) }
        }
    }

    private fun needsDispatch(key: String, current: Long, total: Long, granularity: Float): Boolean {
        if (granularity == 0f || current == 0L || total == current) {
            return true
        }
        val percent = 100f * current / total
        val currentProgress = (percent / granularity).toLong()
        val lastProgress = PROGRESSES[key]
        return if (lastProgress == null || currentProgress != lastProgress) {
            PROGRESSES[key] = currentProgress
            true
        } else {
            false
        }
    }
}
