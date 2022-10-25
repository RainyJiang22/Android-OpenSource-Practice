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
        private val PROGRESSES = HashMap<String?, Long>() //1
        private val LISTENERS = HashMap<String?, OnProgressBarListener>() //2

        internal fun expect(url: String?, listener: OnProgressBarListener) { //3
            LISTENERS[url] = listener
        }

        internal fun forget(url: String?) { //4
            LISTENERS.remove(url)
            PROGRESSES.remove(url)
        }
    }

    private val handler: Handler = Handler(Looper.getMainLooper()) //5

    override fun update(url: HttpUrl, byteRead: Long, contentLength: Long) {
        val key = url.toString()
        //6
        val listener = LISTENERS[key] ?: return
        if (contentLength <= byteRead) {
            //7
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
            //9
            PROGRESSES[key] = currentProgress
            true
        } else {
            false
        }
    }
}
