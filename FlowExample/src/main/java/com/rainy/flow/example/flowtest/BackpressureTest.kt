package com.rainy.flow.example.flowtest


import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.internal.operators.flowable.FlowableOnBackpressureLatest
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch

/**
 * @author jiangshiyu
 * @date 2022/12/13
 */
object BackpressureTest {


    /*  fun bufferTest() {
          val source = PublishSubject.create<Int>()

          source.buffer(1024)
              .observeOn(Schedulers.computation())
              .subscribe(ComputeFunction::compute, Throwable::printStackTrace);
      }*/


    //形象一点就是 一个堵住，一个挂起等待，马桶堵住，等待红绿灯

    //对于Rxjava将会阻塞
    private fun doWorkBlocking(i: Int, delay: Long): Int {
        Thread.sleep(delay)
        return i
    }

    //对于Flow来说它将会挂起，也就是暂停
    private suspend fun doWorkDelay(i: Int, timeout: Long): Int {
        delay(timeout)
        return i
    }

    private fun flowable(delay: Long, mode: BackpressureStrategy, limit: Int): Flowable<Int> =
        Flowable.create<Int>({ emitter ->
            for (i in 1..limit) {
                Thread.sleep(delay)
                emitter.onNext(i)
            }
        }, mode)


    private fun flow(timeout: Long, limit: Int): Flow<Int> = flow {
        for (i in 1..limit) {
            delay(timeout)
            emit(i)
        }
    }


    //by Rxjava
    fun testFlowable(
        mode: BackpressureStrategy,
        limit: Int = 10,
    ) {
        val latch = CountDownLatch(1)
        val stringBuffer = StringBuffer()
        val time = System.currentTimeMillis()

        flowable(delay = 100, mode = mode, limit = limit)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation(), false, 1)
            .map { doWorkBlocking(i = it, delay = 200) }
            .map { doWorkBlocking(i = it, delay = 300) }
            .doOnComplete { latch.countDown() }
            .subscribe {
                stringBuffer.append("$it ")
            }

        latch.await()

        println((System.currentTimeMillis() - time))
        println(stringBuffer.toString())
    }

    //by flow
    fun testFlow(limit: Int = 10, onBackpressure: Flow<Int>.() -> Flow<Int>) {

        val latch = CountDownLatch(1)
        val time = System.currentTimeMillis()

        val stringBuffer = StringBuffer()

        CoroutineScope(Job() + Dispatchers.Default).launch {

            flow(timeout = 100, limit = limit)
                .flowOn(Dispatchers.IO)
                .onBackpressure()
                .map { doWorkDelay(i = it, timeout = 200) }
                .map { doWorkDelay(i = it, timeout = 300) }
                .onCompletion { latch.countDown() }
                .collect {
                    stringBuffer.append("$it")
                }
        }

        latch.await()
        println((System.currentTimeMillis() - time))
        println(stringBuffer.toString())
    }
}

fun main() {
    //test latest
    BackpressureTest.testFlowable(BackpressureStrategy.LATEST)
    //flow Latest
    BackpressureTest.testFlow { conflate() }


    //drop
    BackpressureTest.testFlowable(BackpressureStrategy.DROP)
    //flow in drop 参考stackoverflow
    BackpressureTest.testFlow { onBackpressureDrop() }


    //buffer
    BackpressureTest.testFlowable(BackpressureStrategy.BUFFER)
    BackpressureTest.testFlow { buffer() }
    BackpressureTest.testFlowable(BackpressureStrategy.ERROR)
    BackpressureTest.testFlowable(BackpressureStrategy.MISSING)
}

fun <T> Flow<T>.onBackpressureDrop(): Flow<T> {
    return channelFlow {
        collect { offer(it) }
    }.buffer(capacity = 0)
}