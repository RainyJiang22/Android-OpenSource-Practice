package com.rainy.flow.example.flowtest.rx.operator

import android.annotation.SuppressLint
import android.util.Log
import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class OperateWindow : BaseOp() {


    companion object {
        const val TAG = "OperateWindow"
    }

    @SuppressLint("CheckResult")
    fun doWindow() {
        Observable.interval(1, TimeUnit.SECONDS).take(12)
            .window(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getConsumer())
    }

    @SuppressLint("CheckResult")
    private fun getConsumer(): Consumer<Observable<Long>> {
        return Consumer { observable: Observable<Long> ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { value: Long ->
                    Log.d(TAG, "Next:$value")
                }
            Log.w(TAG, "-----插入操作开始------")
        }
    }

}