package com.rainy.android_opensource_practice

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.rainy.android_opensource_practice.databinding.ActivityMainBinding
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2023/4/18
 */
class TestActivity : BaseActivity<BaseViewModel,ActivityMainBinding>() {

    private val lifecycleOwner:LifecycleOwner?=null
    override fun onBundle(bundle: Bundle) {
    }

    override fun createObserver() {
    }

    override fun initView(savedInstanceState: Bundle?) {


    }
}