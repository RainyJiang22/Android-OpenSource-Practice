package com.rainy.flow.example.download

import java.io.File

/**
 * @author jiangshiyu
 * @date 2022/10/14
 */
sealed class DownloadStatus {

    object None : DownloadStatus()

    data class Progress(val value: Int) : DownloadStatus() //下载进度

    data class Error(val throwable: Throwable) : DownloadStatus() //错误

    data class Done(val file: File) : DownloadStatus() //完成

}
