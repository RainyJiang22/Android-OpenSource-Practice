package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class OperateTimer : BaseOp() {

    companion object {
        const val TAG = "OperateTimer"
    }


    /*
	 *  timer操作符，延迟指定时间，开始发送long类型的指令，类似于Interval.但是这发送一条
	 */
    fun doTimer() {
        Observable.timer(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Long> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Long) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            })
    }


}