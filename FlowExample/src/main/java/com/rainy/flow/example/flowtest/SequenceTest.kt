package com.rainy.flow.example.flowtest

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull

/**
 * @author jiangshiyu
 * @date 2023/5/6
 */
object SequenceTest {

    fun simple() = sequence {
        (1..3).forEach {
            Thread.sleep(100)
            println("generating $it")
            yield(it)
        }
    }
}
fun main1() = runBlocking {
    launch {
        for (k in 1..3) {
            println("From main $k")
            delay(100)
        }
    }


    SequenceTest.simple().forEach { value ->
        println("From sequence $value")
    }
    withTimeoutOrNull(250) {
        SequenceTest.simple().forEach { value ->
            println(value)
        }
        println("Done")
    }
}




