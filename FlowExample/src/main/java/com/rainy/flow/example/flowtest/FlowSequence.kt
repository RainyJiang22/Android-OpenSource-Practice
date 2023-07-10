package com.rainy.flow.example.flowtest

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

/**
 * @author jiangshiyu
 * @date 2023/5/6
 *
 * Kotlin Flow vs Sequence
 */

fun main() {
    FlowSequence.testSortyByKotlin()
}

data class SortListBean(
    val sort: Int,
    val name: String,
)


val list = arrayListOf<SortListBean>(
    SortListBean(1, "James"),
    SortListBean(4, "Jingle"),
    SortListBean(3, "Rain"),
    SortListBean(3, "Rain")
)


object FlowSequence {


    fun testSortyByKotlin() {
        val list = list.sortBy { it.sort }
    }

    fun testExample() {
        (1..3).asSequence()
            .map {
                println("sequence map $it")
                it * 2
            }
            .first {
                it > 2
            }.let {
                println("sequence $it")
            }
        (1..3).asSequence()
            .map {
                println("sequence map $it")
                it * 2
            }


        runBlocking {
            (1..3).asFlow()
                .map {
                    println("flow map $it")
                    it * 2
                }
                .first {
                    it > 2
                }.let {
                    println("flow $it")
                }
        }
    }

    fun expandExample() {

        //flow 很容易进行扩展，但是sequence不行
        runBlocking {
            (2..5 step 2).asFlow().transform {
                emit(it - 1)
            }
            (2..6 step 2).asFlow().transform {
                emit(it - 1)
                emit(it)
            }.collect {
                println(it)
            }
        }

    }

    fun threadExample() {

        //如果我们想在另一个线程中启动我们的序列，则必须通过单独的工具（例如协程）来完成
        CoroutineScope(Dispatchers.IO).launch {
            (1..3).asSequence().forEach {
                println("$it ${Thread.currentThread()}")
            }
        }
        Thread.sleep(100)

        //轻松在另一个协程中启动，通过launchIn函数,提供了作用域
        (1..3).asFlow()
            .onEach {
                println("$it ${Thread.currentThread()}")
            }.launchIn(CoroutineScope(Dispatchers.IO))


    }


    fun flowOnExample() {
        runBlocking {
            flow {
                (1..3).forEach {
                    println("Fire $it ${Thread.currentThread()}")
                    emit(it)
                }
            }.flowOn(Dispatchers.IO)
                .transform {
                    println("Operate $it ${Thread.currentThread()}")
                    emit(it)
                }
                .flowOn(Dispatchers.Default).collect {
                    println("Collect $it ${Thread.currentThread()}")
                }
        }
    }


    fun processExample() {
        runBlocking {
            val sequenceTime = measureTimeMillis {
                sequence {
                    (1..3).forEach {
                        Thread.sleep(100)
                        yield(it)
                    }
                }.forEach {
                    delay(300)
                }
            }
            println("Collected in $sequenceTime ms")


            val flowTime = measureTimeMillis {
                flow {
                    for (i in 1..3) {
                        delay(100)
                        emit(i)
                    }
                }.buffer().collect {
                    delay(300)
                }
            }
            println("Collected in $flowTime ms")
        }
    }


    fun conflateExample() {
        runBlocking {
            val time = measureTimeMillis {
                flow {
                    for (i in 1..3) {
                        delay(100)
                        emit(i)
                    }
                    for (i in 1..3) {
                        delay(100)
                        emit(i)
                    }
                    for (i in 1..3) {
                        delay(100)
                    }
                    for (i in 1..3) {
                        delay(100)
                        emit(i)
                    }
                }.conflate().collect {
                    delay(200)
                    println(it)
                }
            }
            println("Collected in $time ms")
        }
    }

}