## 关于SampleEventBus(手写简单的EventBus)

> 核心重点就在于其通过**注解处理器**生成辅助文件这个过程

### 提供注解处理器

提供一个注解对监听方法进行标记

```kotlin
@MustBeDocumented
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class Event
```

