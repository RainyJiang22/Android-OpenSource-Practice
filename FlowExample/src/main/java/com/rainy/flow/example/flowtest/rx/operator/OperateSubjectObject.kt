package com.rainy.flow.example.flowtest.rx.operator

import com.rainy.flow.example.flowtest.rx.operator.base.BaseOp
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject

/**
 * @author jiangshiyu
 * @date 2023/2/1
 */
class OperateSubjectObject : BaseOp() {

    companion object {
        const val TAG = "OperateSubjectObject"
    }

    fun doAsync() {

        //AsyncSubject 操作符，只有在observer发送complete之后，才会生效。会发送最后一个指令，以及 complete指令。
        val asyncSubject = AsyncSubject.create<String>()

        asyncSubject.onNext("a")
        asyncSubject.onNext("b")
        asyncSubject.subscribe(getObserver(TAG, "first"))
        asyncSubject.onNext("c")
        asyncSubject.onNext("d")
        asyncSubject.onNext("e")
        asyncSubject.subscribe(getObserver(TAG, "second"))
        asyncSubject.onNext("f")
        asyncSubject.onComplete()

        /*
           first d.isDisposed():false
           second d.isDisposed():false
           first f
           first onComplete
           second f
           second onComplete
         */
    }

    fun doBehavior() {
        //不同于Async的发送Complete以及发送最后一条指令
        //Behavior会发送切换observer之前的一条数据，然后也同步发送给所有observer，切换后的指令

        val behaviorSubject = BehaviorSubject.create<String>()
        behaviorSubject.onNext("a")
        behaviorSubject.onNext("b")
        behaviorSubject.subscribe(getObserver(TAG, "first"))
        behaviorSubject.onNext("c")
        behaviorSubject.onNext("d")
        behaviorSubject.subscribe(getObserver(TAG, "second"))
        behaviorSubject.onNext("e")
        behaviorSubject.onNext("f")
        behaviorSubject.onComplete()

        /*
          first d.isDisposed():false
          first b
          first c
          first d
          second d.isDisposed():false
          second d
          first e
          second e
          first f
          second f
          first onComplete
          second onComplete
       */
    }


    fun doPublish() {
        //发送已经操作过的指令。并且会将后来的指令，同步发送所有在线的observer，有的操作符，只会保留一个连接
        val publishSubject = PublishSubject.create<String>()
        publishSubject.onNext("A")
        publishSubject.subscribe(getObserver(TAG, "first"))
        publishSubject.onNext("B")
        publishSubject.onNext("C")
        publishSubject.onNext("D")
        publishSubject.subscribe(getObserver(TAG, "second"))
        publishSubject.onNext("E")
        publishSubject.onNext("F")
        publishSubject.onComplete()
    }


    fun doReply() {
        //replay操作符 重复发送之前所有的操作符,保持原有顺序
        val replaySubject = ReplaySubject.create<String>()
        replaySubject.onNext("A")
        replaySubject.subscribe(getObserver(TAG, "first"))
        replaySubject.onNext("B")
        replaySubject.onNext("C")
        replaySubject.onNext("D")
        replaySubject.onNext("E")
        replaySubject.onNext("F")
        replaySubject.onComplete()
        replaySubject.subscribe(getObserver(TAG, "second"))

        /*
      First d.isDisposed():false
      First A
      First B
      First C
      First D
      First E
      First F
      First onComplete
      Second d.isDisposed():false
      Second A
      Second B
      Second C
      Second D
      Second E
      Second F
      Second onComplete
       */
    }

}