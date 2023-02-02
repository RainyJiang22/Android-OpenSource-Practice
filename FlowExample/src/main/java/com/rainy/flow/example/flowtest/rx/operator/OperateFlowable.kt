package com.rainy.flow.example.flowtest.rx.operator

import android.util.Log
import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.internal.operators.flowable.FlowableFromObservable

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class OperateFlowable : BaseOp() {

    companion object {
        const val TAG = "OperateFlowable"
    }

    fun doFlowable() {
        FlowableFromObservable.just("a", "b", "c", "dd", "ad", "ce", "f")
            .reduce("seed")
            //指定某个算法, 在seed上依次操作所有数据，可以自定义算法，这里的操作符进行简单的拼接
            { s: String, s2: String -> s + s2 }.subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "onSubscribe: ${d.isDisposed}")
                }

                override fun onSuccess(s: String) {
                    Log.w(TAG, "onSuccess: $s")
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: " + e.message)
                }
            })
    }
}