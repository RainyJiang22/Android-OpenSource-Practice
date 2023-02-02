package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
class ObserverDebounce : BaseOp() {

    companion object {
        const val TAG = "ObserverDebounce"
    }

    fun doDebounce() {

        getObservable().debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getObserver(TAG,""))
    }


    private fun getObservable(): Observable<String> {
        return Observable.create { emitter->

            // send events with simulated time wait
            emitter.onNext("a") // skip
            Thread.sleep(400)
            emitter.onNext("b") // deliver
            Thread.sleep(505)
            emitter.onNext("c") // skip
            Thread.sleep(100)
            emitter.onNext("d") // deliver，3和4属于同一个500ms间隔
            Thread.sleep(605)
            emitter.onNext("e") // deliver
            Thread.sleep(510)
            emitter.onComplete()
        }
    }
}