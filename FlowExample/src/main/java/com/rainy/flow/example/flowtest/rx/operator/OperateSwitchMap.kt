package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */


class OperateSwitchMap : BaseOp() {


    companion object {
        const val TAG = "OperateSwitchMap"
    }

    /*
	 *  switchMap 转换输入流的Observable
	 */
    fun doSwitchMap() {
        Observable.just("A", "B", "C", "D", "E", "F")
            .switchMap { s: String ->
                Observable.just("$s @@")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver(TAG, ""))
    }
}