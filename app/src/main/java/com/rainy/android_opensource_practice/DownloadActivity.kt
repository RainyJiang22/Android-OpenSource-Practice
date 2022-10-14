package com.rainy.android_opensource_practice

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ToastUtils
import com.rainy.android_opensource_practice.databinding.ActivityDownloadBinding
import com.rainy.flow.example.download.DownloadManager
import com.rainy.flow.example.download.DownloadStatus
import kotlinx.coroutines.flow.collect
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import java.io.File

/**
 * @author jiangshiyu
 * @date 2022/10/14
 */
class DownloadActivity : BaseActivity<BaseViewModel, ActivityDownloadBinding>() {

    private val url = "https://remotebook.oss-cn-hangzhou.aliyuncs.com/S.AsyncSubject.e.png"

    override fun initView(savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenCreated {

            val file = File(this@DownloadActivity.applicationContext.filesDir.path, "pic.png")

            DownloadManager.download(url, file).collect { status ->
                when (status) {

                    is DownloadStatus.Progress -> {
                        mViewBind.progressbar.progress = status.value
                    }

                    is DownloadStatus.Error -> {
                        ToastUtils.showShort("下载错误")
                    }

                    is DownloadStatus.Done -> {
                        mViewBind.progressbar.progress = 100

                        Log.d("downloading", "initView: ${status.file.path}")
                        ToastUtils.showShort("下载完成")


                    }
                    else -> {
                        Log.d("downloading", "initView: $")
                    }
                }
            }
        }
    }


}