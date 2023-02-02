package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
class OperateDelay : BaseOp() {

    companion object {
        const val TAG = "OperateDelay"
    }

    //delay操作符，延迟操作
    fun doDelay() {
        Observable.just("abc")
            .delay(1000,TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver(TAG,""))
    }

}