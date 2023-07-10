package com.rainy.flow.example.flowtest

import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.Dispatcher
import kotlin.system.measureTimeMillis

/**
 * @author jiangshiyu
 * @date 2022/11/3
 */
class FlowTest {


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


    //completable in Rxjava
    fun completableRequest(): Completable {
        return Completable.create { emitter ->
            try {
                emitter.onComplete()
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
    }

    fun main() {
        completableRequest()
            .subscribe {
                println("I,am done")
                println()
            }
    }

    fun completableCoroutine() = runBlocking {
        try {
            delay(500L)
            println("I am done")
        } catch (e: Exception) {
            println("Got an exception")
        }
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

//fun main() {
//    runBlocking {
//        val time = measureTimeMillis {
//            createEmitter().collectLatest {
//                print("\nCollect $it starts ${start - currentTime()}ms")
//                delay(3000L)
//                println("   Collect $it ends ${currentTime() - start}ms")
//            }
//        }
//        print("\nCollected in $time ms")
//    }
//}

/*fun getUsernames(): Flowable<String> {
    return Flowable<String>()
}


fun getUserFromNetwork(): Single<User> {
    //.....
}*/


fun userNameFlow() = flow<User> {
    return@flow
}


data class User(val userName: String)


fun compareFlatMap() {
    /*getUsernames() //Flowable<String>
        .flatMapSingle { username ->
            getUserFromNetwork(username) // Single<User>
        }
        .subscribe(
            { user -> println(user) },
            { println("Got an exception") }
        )*/
    runBlocking {
        flow {
            emit(User("Jacky"))
        }.map {
            getUserFromName(it)
        }.collect {
            println(it)
        }
    }


}

suspend fun getUserFromName(user: User): String {
    return user.userName
}

fun getUsernames(): Flowable<String> {
    val flowableEmitter = { emitter: FlowableEmitter<String> ->
        emitter.onNext("Jacky")
    }
    return Flowable.create(flowableEmitter, BackpressureStrategy.BUFFER)
}

fun isCorrectUserName(userName: String): Single<Boolean> {
    return Single.create { emitter ->
        runCatching {
            //名字判断....
            if (userName.isNotEmpty()) {
                emitter.onSuccess(true)
            } else {
                emitter.onSuccess(false)
            }
        }.onFailure {
            emitter.onError(it)
        }
    }
}


fun compareFilter() {
    getUsernames()//Flowable<String>
        .flatMapSingle { userName ->
            isCorrectUserName(userName) //Single<Boolean>
                .flatMap { isCorrect ->
                    if (isCorrect) {
                        Single.just(userName)
                    } else {
                        Single.never()
                    }
                }
        }.subscribe {
            println(it)
        }

    runBlocking {
        userNameFlow().filter { user ->
            isCorrectName(user.userName)
        }.collect { user ->
            println(user)
        }
    }

}

suspend fun isCorrectName(userName: String): Boolean {
    return userName.isNotEmpty()
}

fun RxBackpressureDropTest() {
    Flowable.range(1, 1_000_000)
        .onBackpressureDrop()
        .observeOn(Schedulers.single(), false, 1)
        .subscribe { value ->
            Thread.sleep(100)
            println("Got value:$value")
        }

    Thread.sleep(1000)
}

fun FlowBackpressureDropTest() {
    runBlocking {
        (1..1_000_000).asFlow()
            .buffer(capacity = 1, onBufferOverflow = BufferOverflow.DROP_LATEST)
            .collect { value ->
                delay(100)
                println("Got value: $value")
            }
    }
}

fun RxBackpressureLatestTest() {
    Flowable.range(1, 1_000_000)
        .onBackpressureLatest()
        .observeOn(Schedulers.single(), false, 2)
        .subscribe { value ->
            Thread.sleep(100)
            println("Got value: $value")
        }
    Thread.sleep(1000L)
}

fun FlowBackpressureLatestTest() {
    runBlocking {
        (1..1_000_000).asFlow()
            .buffer(capacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
            .collect { value ->
                delay(100)
                println("Got value: $value")
            }
    }
}

fun RxBackpressureBufferTest() {
    Flowable.range(1, 1_000_000)
        .onBackpressureBuffer()
        .observeOn(Schedulers.single())
        .subscribe { value ->
            Thread.sleep(100)
            println("Got value: $value")
        }
}

fun FlowBackpressureBufferTest() {
    runBlocking {
        (1..1_000_00).asFlow()
            .buffer(capacity = 0, onBufferOverflow = BufferOverflow.SUSPEND)
            .collect { value ->
                delay(100)
                println("Got value: $value")
            }
    }
}

fun getLocationFlow(): Flow<Location> {
    return callbackFlow {

        val locationListener = LocationListener {


        }
//        LocationManagerCompat.registerGnssStatusCallback()

//        val locationListener = LocationListener{
//            trySend(it)
//        }
//
//        LocationManager.registerForLocation(locationListener)
//
//        awaitClose {
//            LocationManager.unregisterForLocation(locationListener)
//        }
    }
}

//fun simple() = flow {
//    (1..3).forEach {
//        delay(100)
//        emit(it)
//    }
//}
//
//fun main() = runBlocking {
//    launch {
//        for (k in 1..3) {
//            println("From main $k")
//            delay(100)
//        }
//    }
//
//    simple().collect { value -> println("From flow $value") }
//}

//val sequence = (1..100000000).asSequence()
//val startTime = System.currentTimeMillis()
//fun main() = runBlocking {
//    val result = sequence
//        .map { it * 3 }
//        .filter { it % 2 == 0 }
//    println("Start")
//    result.reduce { ac, it ->
//        ac + it
//    }.run { println(this) }
//    println("Done in ${System.currentTimeMillis() - startTime}ms")
//}


//val flows = (1..100000000).asFlow()
//val sequence = (1..10000000).asSequence()
//val startTime = System.currentTimeMillis()
//fun main() = runBlocking {
//
//    for (i in (1..3).asSequence()) {
//        println(i)
//    }
//
//    (1..3).asSequence().asFlow()
//
//    val result = flows.map { it * 3 }
//        .filter { it % 2 == 0 }
//    println("Start")
//    result.reduce { ac, value ->
//        ac + value
//    }.run { println(this) }
//    println("Done in ${System.currentTimeMillis() - startTime}ms")
//}

//fun main() {
//
//    //在协程范围中
//    runBlocking {
//        (1..3).asFlow().collect()
//    }
//
//    //在挂起函数中
//    suspend fun main() {
//        (1..3).asFlow().collect()
//    }
//
//    //使用launchIn设置协程范围
//    (1..3).asFlow().launchIn(CoroutineScope(Dispatchers.IO))
//}


//fun simple() = sequence {
//    (1..3).forEach {
//        Thread.sleep(100)
//        yield(it)
//    }
//}
//
//fun main() = runBlocking {
//    //250ms后超时
//    withTimeoutOrNull(250) {
//        simple().forEach { value -> println(value) }
//    }
//    withTimeoutOrNull(250) {
//        simple().forEach { value ->  }
//    }
//    println("Done")
//}


//fun simple() = flow {
//    (1..3).forEach {
//        delay(100)
//        emit(it)
//    }
//}
//
//fun main() = runBlocking {
//    //250ms后超时
//    withTimeoutOrNull(250) {
//        simple().collect { value -> println(value) }
//    }
//    println("Done")
//}
//
//
//fun main() = runBlocking {
//    (2..6 step 2).asFlow().transform {
//        emit(it - 1)
//        emit(it)
//    }.collect { println(it) }
//
//    (2..5 step 2).asSequence().forEach {
//        println(it)
//    }
//}

//fun main() {
//    run()
//    //确保其他线程完成
//    Thread.sleep(100)
//}
//
//fun run() {
//    CoroutineScope(Dispatchers.IO).launch {
//        (1..3).asSequence()
//            .forEach {
//                println("$it ${Thread.currentThread()}")
//            }
//    }
//}

//fun main() {
//    run()
//    // 确保其他线程完成
//    Thread.sleep(100)
//}
//
//fun run() {
//    (1..3).asFlow()
//        .onEach {
//            println("$it ${Thread.currentThread()}")
//        }.launchIn(CoroutineScope(Dispatchers.IO))
//}

//fun main() = runBlocking {
//    flow {
//        (1..3).forEach {
//            println("Fire $it ${Thread.currentThread()}")
//            emit(it)
//        }
//    }
//        .flowOn(Dispatchers.IO)
//        .transform {
//            println("Operate $it ${Thread.currentThread()}")
//            emit(it)
//        }
//        .flowOn(Dispatchers.Default)
//        .collect {
//            println("Collect $it ${Thread.currentThread()}")
//        }
//}

//fun simple() = sequence {
//    (1..3).forEach {
//        Thread.sleep(100)
//        yield(it)
//    }
//}
//
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        simple().forEach {
//            delay(300)
//        }
//    }
//    println("Collected in $time ms")
//}


//fun simple(): Flow<Int> = flow {
//    for (i in 1..3) {
//        delay(100)
//        emit(i)
//    }
//}
//
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        simple().buffer().collect {
//            delay(300)
//        }
//    }
//    println("Collected in $time ms")
//}

//fun simple(): Sequence<Int> = sequence {
//    for (i in 1..3) {
//        println("Generating $i")
//        yield(i)
//    }
//}
//
//fun main() = runBlocking {
//    try {
//        simple().forEach { value ->
//            check(value <= 1) { "Crash on $value" }
//            println("Got $value")
//        }
//    } catch (e: Throwable) {
//        println("Caught $e")
//    } finally {
//        println("Done")
//    }
//}

//fun simple(): Flow<Int> = flow {
//    for (i in 1..3) { delay(100); emit(i) }
//}
//
//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        simple().collectLatest {
//            println("get $it")
//            delay(300)
//            println("done $it")
//        }
//    }
//    println("Collected in $time ms")
//}


//使用这个zip运算符
fun firstSeq() = sequence {
    (1..3).forEach {
        Thread.sleep(100)
        yield(it)
    }
}

fun secondSeq() = sequence {
    (4..6).forEach {
        Thread.sleep(300)
        yield(it)
    }
}

fun firstFlow() = flow {
    (1..3).forEach {
        delay(100)
        emit(it)
    }
}

fun secondFlow() = flow {
    (4..6).forEach {
        delay(300)
        emit(it)
    }
    (4..19).forEach {
        delay(3000)
        emit(it)
    }
}

//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        firstFlow().zip(secondFlow()) { first, second ->
//            Pair(first, second)
//        }.collect {
//            println(it)
//        }
//    }
//    println("Collected in $time ms")
//}

//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        firstFlow().combine(secondFlow()) { first, second ->
//            Pair(first, second)
//        }.collect { println(it) }
//    }
//    println("Collected in $time ms")
//}


//fun main() = runBlocking {
//    val time = measureTimeMillis {
//        firstSeq().zip(secondSeq()).forEach {
//            println(it)
//        }
//    }
//    println("Collected in $time ms")
//}


//fun requestSequence(i: Int): Sequence<String> = sequence {
//    yield("$i: First")
//    Thread.sleep(300)
//    yield("$i: Second")
//}
//
//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    (1..3).asSequence().onEach {
//        Thread.sleep(100)
//    }.flatMap {
//        requestSequence(it)
//    }.forEach {
//        println("$it 从开始耗时 ${System.currentTimeMillis() - startTime} ms")
//    }
//}





//@OptIn(FlowPreview::class)
//fun main() = runBlocking {
//    val startTime = System.currentTimeMillis()
//    (1..3).asFlow().onEach { delay(100) }
//        .flatMapMerge {
//            otherFlow(it)
//        }
//        .collect { value ->
//            println("$value 从开始耗时 ${System.currentTimeMillis() - startTime} ms ")
//        }
//}


fun otherFlow(i: Int): Flow<String> = flow {
    emit("$i:First")
    delay(300)
    emit("$i:Second")
}


@OptIn(ExperimentalCoroutinesApi::class)
fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    (1..3).asFlow().onEach { delay(100) }
        .flatMapLatest { otherFlow(it) }
        .collect { value ->
            println("$value 从开始耗时 ${System.currentTimeMillis() - startTime} ms ")
        }
}
