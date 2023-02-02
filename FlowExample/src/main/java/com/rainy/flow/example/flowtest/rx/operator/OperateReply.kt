package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.subjects.PublishSubject

/**
 * @author jiangshiyu
 * @date 2023/2/2
 */

class OperateReply : BaseOp() {


    companion object {
        const val TAG = "OperateReply"
    }

    /*
	 *  根据设定的replay容量大小，来缓存一个的数据指令，给下一个observer，即使是已经complete。
	 *  需要配合ConnectableObservable使用
	 *
	 */
    fun doReplay() {
        val subject = PublishSubject.create<String>()
        val connectableObservable = subject.replay(3) //默认是全部
        connectableObservable.connect() //开启关联


        subject.onNext("A")

        connectableObservable.subscribe(getObserver(TAG,"first"))

        subject.onNext("B")
        subject.onNext("C")
        subject.onNext("D")

        connectableObservable.subscribe(getObserver(TAG,"second"))


        subject.onNext("E")
        subject.onNext("F")
        subject.onComplete()

        //由于只缓冲了三个容量
        //只有D、E、F 3 个 以及complete指令，
        connectableObservable.subscribe(getObserver(TAG, "Third"))


    }
}