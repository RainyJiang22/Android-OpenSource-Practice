package com.rainy.android_opensource_practice.flowtest

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

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
        .subscribeOn(Schedulers.io()) //指定被观察者（发送的事件）在io线程
        .observeOn(AndroidSchedulers.mainThread()) //指定观察者（接收订阅的事件）在主线程


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


    //创建flow的不同种方式

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


            listOf(1, 2, 3, 4).asFlow().onEach {
                delay(1000L)
            }.collect {
                println(it)
            }

            @OptIn(ExperimentalCoroutinesApi::class)
            channelFlow {
                for (i in 1..5) {
                    delay(1000L)
                    send(i)
                }
            }.collect {
                println(it)
            }
        }
    }


}

fun currentTime() = System.currentTimeMillis()
fun threadName() = Thread.currentThread().name
var start: Long = 0

fun createEmitter(): Flow<Int> =
    (1..5)
        .asFlow()
        .onStart { start = currentTime() }
        .onEach {
            delay(1000L)
            print("Emit $it (${currentTime() - start}ms) ")
        }

fun main() {
    runBlocking {
        val time = measureTimeMillis {
            createEmitter().collectLatest {
                print("\nCollect $it starts ${start - currentTime()}ms")
                delay(3000L)
                println("   Collect $it ends ${currentTime() - start}ms")
            }
        }
        print("\nCollected in $time ms")
    }
}