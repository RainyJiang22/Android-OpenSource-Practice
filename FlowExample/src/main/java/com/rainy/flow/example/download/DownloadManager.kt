package com.rainy.flow.example.download


import com.rainy.flow.example.copyTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.http.Body
import java.io.File
import java.io.IOException

/**
 * @author jiangshiyu
 * @date 2022/10/14
 */
object DownloadManager {


    /**
     * 文件下载
     * @url 下载路径
     * @file 本地保存文件
     */
    fun download(url: String, file: File): Flow<DownloadStatus> {
        return flow {
            val request = Request.Builder().url(url).get().build()
            val response = OkHttpClient.Builder().build().newCall(request).execute()
            if (response.isSuccessful) {
                response.body().let { body ->
                    val total = body?.contentLength()
                    file.outputStream().use { output ->
                        val input = body?.byteStream()
                        var emittedProgress = 0L
                        //使用对应的扩展函数 ，因为该函数最后参为内联函数，因此需要在后面实现对应业务逻辑
                        input?.copyTo(output) { bytesCopied ->
                            //获取下载进度百分比
                            val progress = bytesCopied * 100 / total!!
                            //每下载进度比上次大于5时，通知UI线程
                            if (progress - emittedProgress > 5) {
                                delay(100)
                                //使用Flow对应的emit 发送对应下载进度通知
                                emit(DownloadStatus.Progress(progress.toInt()))
                                //记录当前下载进度
                                emittedProgress = progress
                            }
                        }
                    }
                }
                //发送下载完成通知
                emit(DownloadStatus.Done(file))
            } else {
                throw IOException(response.toString())
            }
        }.catch {
            //下载失败
            file.delete()
            emit(DownloadStatus.Error(it))
        }.flowOn(Dispatchers.IO)
    }
}