package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

class OperateSkip : BaseOp() {

    companion object {
        const val TAG = "OperateSkip"
    }

    /**
     * skip操作符会跳过指定数组的指令
     */
    fun doSomeSkip() {
        Observable.just("A", "B", "C", "D")
            .skip(2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver(TAG, ""))
    }

}