package com.rainy.flow.example

import java.io.InputStream
import java.io.OutputStream

/**
 * @author jiangshiyu
 * @date 2022/10/14
 */

inline fun InputStream.copyTo(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE, progress: (Long)-> Unit): Long {
    var bytesCopied: Long = 0
    val buffer = ByteArray(bufferSize)
    var bytes = read(buffer)
    while (bytes >= 0) {
        out.write(buffer, 0, bytes)
        bytesCopied += bytes
        bytes = read(buffer)
        progress(bytesCopied) //在最后调用内联函数
    }
    return bytesCopied
}

