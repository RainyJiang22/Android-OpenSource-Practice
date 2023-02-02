package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class OperateScan : BaseOp() {

    companion object {
        const val TAG = "OperateScan"
    }

    /**
     * scan操作符，向下依次遍历
     */
    fun doOperateScan() {
        Observable.just("A","B","C","D","E","F")
            .scan { t1, t2 ->
                t1 + t2
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver(TAG,""))
    }
}