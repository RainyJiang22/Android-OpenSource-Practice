package com.rainy.android_opensource_practice.net

import android.os.Bundle
import android.util.Log
import com.rainy.android_opensource_practice.BaseActivity
import com.rainy.android_opensource_practice.databinding.ActivityUserBinding

/**
 * @author jiangshiyu
 * @date 2022/10/17
 */
class HttpTestActivity : BaseActivity<BannerViewModel, ActivityUserBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mViewModel.getBanner()
    }

    override fun createObserver() {

        mViewModel.bannerDataState.observe(this) {
            Log.d("Test", "createObserver: $it")
        }

    }

    override fun onBundle(bundle: Bundle) {

    }
}