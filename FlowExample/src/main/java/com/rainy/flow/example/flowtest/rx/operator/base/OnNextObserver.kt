package com.rainy.flow.example.flowtest.rx.operator.base

import android.util.Log
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
open class OnNextObserver<T>(val tag: String, val flag: String) : Observer<T> {
    override fun onSubscribe(d: Disposable) {
        Log.d(tag, "$flag d.isDisposed(): ${d.isDisposed}")
    }

    override fun onError(e: Throwable) {
        Log.e(tag, "$flag error:$e")
    }

    override fun onComplete() {
        Log.i(tag, "$flag onComplete ")
    }

    override fun onNext(t: T) {
    }
}