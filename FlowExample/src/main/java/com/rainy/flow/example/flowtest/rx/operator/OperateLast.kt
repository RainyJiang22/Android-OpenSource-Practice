package com.rainy.flow.example.flowtest.rx.operator

import android.util.Log
import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */
class OperateLast : BaseOp() {

    companion object {
        const val TAG = "OperateLast"
    }

    /*
	 * last操作符，只会发送最后一条指令，若无，则发送默认指令
	 * 这里也使用了SingleObserver，类似的还有consumer等等
	 */
    fun doSome() {
        Observable.just("A1", "A2", "A3", "A4", "A5", "A6")
            .last("A0")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<String> {
                override fun onSubscribe(d: Disposable) {
                    Log.d(TAG, "onSubscribe: ${d.isDisposed}")
                }

                override fun onSuccess(s: String) {
                    Log.i(TAG, "onSuccess: $s")
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: " + e.message)
                }
            })
    }

}