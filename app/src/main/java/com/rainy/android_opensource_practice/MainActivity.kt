package com.rainy.android_opensource_practice

import android.os.Bundle
import android.util.Log
import com.rainy.android_opensource_practice.databinding.ActivityMainBinding
import com.rainy.easybus.SubscribeEnv
import com.rainy.easybus.extention.post
import com.rainy.easybus.extention.subscribeEvent

class MainActivity : BaseActivity() {

    override val bind by getBind<ActivityMainBinding>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        bind.world.setOnClickListener {
            this.post(HelloBean("this is bean world"), isStick = false)
//            startActivity(Intent(this, EventBustActivity::class.java))
        }

        this.subscribeEvent(HelloBean::class.java, SubscribeEnv.MAIN) {
            Log.i(
                "EasyBus",
                "main activity receive a message $it current thread ${Thread.currentThread()}"
            )
            bind.text2.text = it.data
        }


    }
}