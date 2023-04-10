package com.rainy.android_opensource_practice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.rainy.android_opensource_practice.databinding.ActivityMainBinding
import com.rainy.easybus.SubscribeEnv
import com.rainy.easybus.extention.observeEvent
import com.rainy.easybus.extention.postEvent
import com.rainy.glidektx.glide.GlideImageLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {

    private var imageLoader: GlideImageLoader? = null

    override fun createObserver() {
    }


    override fun initView(savedInstanceState: Bundle?) {
        /*this.postEvent(HelloBean("this is bean world"), isStick = false)

        postEvent(HelloBean("this is message"))
        mViewBind.world.setOnClickListener {
            startActivity(Intent(this, EventBustActivity::class.java))
        }
        this.observeEvent(HelloBean::class.java, SubscribeEnv.MAIN) {
            Log.i(
                "EasyBus",
                "main activity receive a message $it current thread ${Thread.currentThread()}"
            )
            mViewBind.text2.text = it.data
        }


        observeEvent<HelloBean> {
            Log.d("EasyBus", "observeEvent is $it")
            mViewBind.text2.text = it.data

        }*/

/*        imageLoader = GlideImageLoader(mViewBind.ivResult, mViewBind.progressbar)

        imageLoader?.load(
            "https://source.unsplash.com/random",
            RequestOptions()
                .override(500, 500)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(mViewBind.ivResult.drawable))*/

    }

    override fun onBundle(bundle: Bundle) {

    }
}