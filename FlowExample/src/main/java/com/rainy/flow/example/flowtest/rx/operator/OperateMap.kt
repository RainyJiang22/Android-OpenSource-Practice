package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class OperateMap : BaseOp() {

    companion object {
        const val TAG = "OperateMap"
    }

    fun doMap() {
        Observable.fromArray(1, 23, 453, 123, 51, 54, 2)
            .map {
                "$it str"
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver(TAG, ""))
    }
}