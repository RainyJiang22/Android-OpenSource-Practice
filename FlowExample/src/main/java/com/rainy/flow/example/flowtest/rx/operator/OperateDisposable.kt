package com.rainy.flow.example.flowtest.rx.operator

import android.os.SystemClock
import android.util.Log
import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
class OperateDisposable : BaseOp() {

    companion object {
        const val TAG = "OperateDisposable"
    }

    private val disposable = CompositeDisposable()


    fun doOperateDisposable() {
        disposable.add(Observable.defer {
            //模拟耗时
            SystemClock.sleep(2000)
            Observable.just("one", "two", "three", "four", "five")
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<String?>() {
                override fun onNext(s: String) {
                    Log.w(TAG, "onNext: $s")
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: " + e.message)
                }

                override fun onComplete() {
                    Log.i(TAG, "onComplete")
                }
            }))
    }

    fun shutdown() {
        disposable.clear()
    }
}