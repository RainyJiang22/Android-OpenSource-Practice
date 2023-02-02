package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class OperateInterval : BaseOp() {

    /**
     * interval操作符 间隔指定时间，一直发送数字指令，默认从0开始的long类型
     */
    fun doInterval() {
        Observable.intervalRange(0L,5,0,2,TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }
}