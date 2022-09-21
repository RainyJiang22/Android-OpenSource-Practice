package com.rainy.glidektx.cache

import com.bumptech.glide.load.Key
import com.bumptech.glide.load.model.Headers
import com.bumptech.glide.util.Preconditions

/**
 * @author jiangshiyu
 * @date 2022/9/21
 *
 * 对于一张网络图片来说，其唯一 Key 的生成就依赖于 GlideUrl 类的 `getCacheKey()`方法，
 * 该方法会直接返回网络图片的 Url 字符串。如果 Url 的 token 值会一直变化，
 * 那么 Glide 就无法对应上同一张图片了，导致磁盘缓存完全失效
 * 这里需要手动定义存储磁盘的唯一key
 */
abstract class GlideUrl @JvmOverloads constructor(
    var url: String?,
    var headers: Headers? = Headers.DEFAULT
) : Key {

    private var stringUrl: String? = null

    init {
        url = null
        stringUrl = Preconditions.checkNotEmpty(url)
        headers = Preconditions.checkNotNull(headers)
    }

    open fun getCacheKey(): String? {
        return if (stringUrl != null) stringUrl else Preconditions.checkNotNull(url).toString()
    }
}
