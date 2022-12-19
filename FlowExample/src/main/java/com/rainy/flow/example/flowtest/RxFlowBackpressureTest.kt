package com.rainy.flow.example.flowtest

import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking

/**
 * @author jiangshiyu
 * @date 2022/12/16
 *
 * Rxjava&Backpressure Test
 */
object RxFlowBackpressureTest {


    fun rxBuffer() {
        Flowable.range(1, 10)
            .onBackpressureBuffer()
            .observeOn(Schedulers.single())
            .subscribe { value ->
                Thread.sleep(100)
                println("Get value: $value")
            }
        Thread.sleep(1000)
    }

    fun flowBuffer() = runBlocking {
        (1..10).asFlow()
            .buffer(capacity = 0, onBufferOverflow = BufferOverflow.SUSPEND)
            .collect { value ->
                delay(100)
                println("Get value: $value")
            }

    }

    fun rxDrop() {

        Flowable.range(1, 10)
            .onBackpressureDrop()
            //将默认的缓冲区size改为1
            .observeOn(Schedulers.single(), false, 1)
            .subscribe { value ->
                Thread.sleep(100)
                println("Get Value: $value")
            }
        Thread.sleep(1000)
    }

    fun flowDrop() = runBlocking {
        (1..10).asFlow()
            .buffer(capacity = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)
            .collect { value ->
                delay(100)
                println("Get value: $value")
            }
    }


    fun rxLatest() {
        Flowable.range(1, 10)
            .onBackpressureLatest()
            .observeOn(Schedulers.single(), false, 2)
            .subscribe { value ->
                Thread.sleep(100)
                println("Get value:$value ")
            }
        Thread.sleep(1000L)
    }

    fun flowLatest() = runBlocking {
        (1..10).asFlow()
            .buffer(capacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
            .collect { value ->
                delay(100)
                println("Get Value: $value")
            }
    }

}

fun main() {

    //BackpressureBuffer in Rxjava
    //RxFlowBackpressureTest.rxBuffer()
    //RxFlowBackpressureTest.flowBuffer()
    //RxFlowBackpressureTest.rxDrop()
    // RxFlowBackpressureTest.rxLatest()

    RxFlowBackpressureTest.flowDrop()
    //RxFlowBackpressureTest.flowLatest()
}