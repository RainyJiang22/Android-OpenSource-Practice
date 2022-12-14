- 总线型数据流，内部可以用flow api实现，这里可以采用SharedFlow。
    - SharedFlow里面就维护着一个list，满足了我们消息存放的数据结构
- 相较于EventBus需要注册解除注册，相信各位大佬们肯定很烦了，那么我们就需要自动注册解除注册这种功能啦。
    - 控制了协程生命周期，相当于控制flow的生命周期，LifecycleScopeObserver来实现
- 可以发送粘性事件与非粘性事件
    - 粘性数据的话，就让接收方接受数据的时候，把list里面的数据再发送一遍不就可以了，非粘性数据的话，就不发送由于我们现在想要单个事件的订阅所以可以利用 MutableSharedFlow实现
- 使其可切换线程订阅
    - 协程可以做到呀，Dispatch切换指定响应线程即可
- 足够精简
    - 都是使用扩展函数，非常方便对原有方法的扩展
- 可实现发送和接收核心功能
  -
  MutableSharedFlow实现了flow发送和接收的接口，发送比较简单使用特性即可，接收稍微繁琐点，由于在同一个协程域中，运行是串行的，需要开两个协程域，分别在里面调用collect函数，监听粘性事件与非粘性事件
- 可以支持全局范围的事件,结合生命周期感知

## 初始化

```kotlin
 EventBusInitializer.init(application)
```

## 事件发送

- APP全局范围事件

```kotlin
postEvent(HelloBean("this is message"))
```

- Activity/Fragment范围内事件

```kotlin
LifecycleOwner.postEvent(HelloBean("this is bean world"), isStick = false)
```

## 事件接收

- App全局事件接收

```kotlin
  observeEvent<HelloBean> {
    Log.d("EasyBus", "observeEvent is $it")
    mViewBind.text2.text = it.data
}
```

- Activity/Fragment范围事件接收

```kotlin
   LifecycleOwner.observeEvent(HelloBean::class.java, SubscribeEnv.MAIN) {
    Log.i(
        "EasyBus",
        "main activity receive a message $it current thread ${Thread.currentThread()}"
    )
    mViewBind.text2.text = it.data
}
```