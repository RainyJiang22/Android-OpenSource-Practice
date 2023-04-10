package com.rainy.android_opensource_practice

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.rainy.android_opensource_practice.databinding.ActivityEventBusBinding
import com.rainy.android_opensource_practice.modularbus.SecondActivity
import com.rainy.easybus.data.Event
import com.rainy.easybus.extention.FlowEventBus
import com.rainy.easybus.extention.globalObserveEvent
import com.rainy.easybus.extention.globalPostEvent
import com.rainy.ktretrofit.BuildConfig
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * @author jiangshiyu
 * @date 2022/9/2
 */
class EventBustActivity : BaseActivity<BaseViewModel, ActivityEventBusBinding>() {


    override fun createObserver() {
        /*      globalObserveEvent<HelloBean>(lifecycleScope, false) {
                  toast("$it")
                  Log.d("EventBus", "createObserver:$it")
              }*/

        FlowEventBus.observe<Event.show>(this, Lifecycle.State.STARTED) {
            Log.d("EventBus", "createObserver:$it")
            mViewBind?.testText?.text = it.data.toString()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind?.btnPostString?.setOnClickListener {
            FlowEventBus.post(event = Event.show(HelloBean("RainyJiang")))
//            globalPostEvent(HelloBean("this is message1"))
        }

        mViewBind?.btnPostBean?.setOnClickListener {
            startActivity<SecondActivity>()
        }
    }

    override fun onBundle(bundle: Bundle) {

    }

}


data class HelloBean(val data: String)