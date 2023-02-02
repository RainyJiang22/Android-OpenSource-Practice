package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import java.util.*

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
class OperateFilter : BaseOp() {


    companion object {
        const val TAG = "OperateFilter"
    }

    fun doFilter() {
        Observable.just("a", "b", "c", "ab", "c", "f", "g")
            .filter {
                it.length == 1
            }
            .subscribe(getObserver(TAG, ""))
    }

}