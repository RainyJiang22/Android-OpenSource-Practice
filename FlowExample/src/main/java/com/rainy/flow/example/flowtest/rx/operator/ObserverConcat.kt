package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
class ObserverConcat : BaseOp() {

    companion object {
        const val TAG = "ObserverConcat"
    }

    //组合操作符，保留原有顺序，组合成一个Observable发布
    fun doConcat() {

        val aStrings = arrayOf("A1", "A2", "A3", "A4")
        val bStrings = arrayOf("B1", "B2", "B3")

        val aObservable = Observable.fromArray(*aStrings)
        val bObservable = Observable.fromArray(*bStrings)

        Observable.concat(aObservable, bObservable)
            .subscribe(getObserver(TAG, ""));
    }
}