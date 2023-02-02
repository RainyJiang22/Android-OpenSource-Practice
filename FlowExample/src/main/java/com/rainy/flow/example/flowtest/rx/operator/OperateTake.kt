package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class OperateTake : BaseOp() {

    companion object {
        const val TAG = "OperateTake"
    }

    /**
     * take操作符，摘取指定长度的指令
     *
     * A，B C
     */
    fun doOperateTake() {
        Observable.just("A", "B", "C", "D", "E")
            .take(3)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver(TAG, ""))
    }
}