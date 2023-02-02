package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.ObservableSource
import java.util.concurrent.Callable

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
class OperateDefer : BaseOp() {

    companion object {
        const val TAG = "OperateDefer"
    }

    //defer操作符，延迟加载
    fun doDefer() {
        val car = Car()
        val brandDeferObservable = car.getBrand()
        //set在get调用之后
        car.setBrand("BMW")

        brandDeferObservable.subscribe(getObserver(TAG,""))
    }
}

internal class Car {
    private var brand: String? = null

    //fixme 实现延迟加载 defer操作符
    fun getBrand(): Observable<String> {
        return Observable.defer {
            Observable.just(brand)
        }
    }

    fun setBrand(brand: String?) {
        this.brand = brand
    }
}