package com.rainy.androidcodeutil

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

inline fun <T> Array<T>.percentage(predicate: (T) -> Boolean) =
    filter(predicate).size.toFloat() / size