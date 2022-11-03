package com.rainy.android_opensource_practice.flowtest

import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * @author jiangshiyu
 * @date 2022/11/3
 */
object FlowTest {


    fun updateObservable(): Observable<String> = Observable.create<String?> {
        it.onNext("step 1")
        it.onNext("step 2")
        it.onNext("step 3")
        it.onNext("step 4")
        it.onComplete()
    }.onErrorReturn {
        throw Exception(it.message)
    }


    /**
     * 替换成flow
     */

    fun updateFlow() = flow {
        emit("step 1")
        emit("step 2")
        emit("step 3")
        emit("step 4")
    }.catch {
        throw Exception(it.message)
    }


    fun createFlow() {
        CoroutineScope(Dispatchers.Main.immediate).launch {

            flowOf("Hi")
                .onStart {
                    //do something
                    emit("this is one")
                    println("the thread is ${Thread.currentThread()}")
                }
                .flowOn(Dispatchers.IO)
                .onStart {
                    emit("this is two")
                    println("the thread is ${Thread.currentThread()}")
                }
                .flowOn(Dispatchers.IO)
                .onStart {
                    println("the thread is ${Thread.currentThread()}")
                    emit("this is three")
                }
        }
    }
}