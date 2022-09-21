package com.rainy.glidektx.cache

import android.net.Uri
import java.net.URI
import java.security.MessageDigest

/**
 * @author jiangshiyu
 * @date 2022/9/21
 * 手动定义磁盘缓存时的唯一 Key
 */
abstract class TokenGlideUrl(private val safeUrl: String) : GlideUrl(safeUrl) {

    override fun getCacheKey(): String? {
        val uri = URI(safeUrl)
        val querySplit = uri.query.split("&".toRegex())
        querySplit.forEach {
            val kv = it.split("=".toRegex())
            if (kv.size == 2 && kv[0] == "token") {
                //将包含 token 的键值对移除
                return safeUrl.replace(it, "")
            }
        }
        return safeUrl
    }
}