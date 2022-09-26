package com.rainy.android_opensource_practice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.rainy.android_opensource_practice.databinding.ActivityMainBinding
import com.rainy.easybus.SubscribeEnv
import com.rainy.easybus.extention.post
import com.rainy.easybus.extention.subscribeEvent
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {


    override fun createObserver() {
        super.createObserver()

    }

    override fun initView(savedInstanceState: Bundle?) {
        this.post(HelloBean("this is bean world"), isStick = false)
        mViewBind.world.setOnClickListener {
            eventViewModel.textEvent.value = HelloBean("this is message")
            startActivity(Intent(this, EventBustActivity::class.java))
        }
        this.subscribeEvent(HelloBean::class.java, SubscribeEnv.MAIN) {
            Log.i(
                "EasyBus",
                "main activity receive a message $it current thread ${Thread.currentThread()}"
            )
            mViewBind.text2.text = it.data
        }


    }
}