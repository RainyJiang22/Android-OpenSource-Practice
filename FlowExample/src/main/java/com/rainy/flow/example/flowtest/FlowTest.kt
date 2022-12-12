package com.rainy.android_opensource_practice.flowtest

import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
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