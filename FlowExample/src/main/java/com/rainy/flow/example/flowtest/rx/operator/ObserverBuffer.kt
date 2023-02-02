package com.rainy.flow.example.flowtest.rx.operator

import android.content.Intent
import android.util.Log
import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import com.rainy.flow.example.flowtest.rx.operator.base.OnNextObserver
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
class ObserverBuffer : BaseOp() {

    companion object {
        const val TAG = "ObserverBuffer"
    }

    //使用buffer操作符，根据指定摘取一个list

    fun doSomeBuffer() {
        Observable.just(1, 3, 5, 10, 3)
            .buffer(3, 1)
            .subscribe(object : OnNextObserver<List<Int>>(TAG,"") {
                override fun onNext(t: List<Int>) {
                    for (i in t) {
                        Log.w(TAG, "onNext: $i")
                    }
                }
            })

    }
}