package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.Observer

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
class OperateDistinct : BaseOp() {

    companion object {
        const val TAG = "OperateDistinct"
    }

    //distinct自动去重复，后面若出现前面已经有数据的时候，会自动跳过
    fun doDistinct() {
        Observable.just("a", "b", "c", "f", "ad", "c", "f", "g")
            .distinct()
            .subscribe(getObserver(TAG, ""))
    }
}