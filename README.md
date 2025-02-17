# application-context-util

---

## Overview

Application context util is a simple kotlin based library that provides for setting up and using simple key/value context in our application.
Sure key/value pairs can be managed by a simple Map but then passing that map all around the code methods is very cumbersome.

What this library aims is to enable to create and access context anywhere in code statically without needing to pass around in methods meanwhile providing thread/coroutine safety as well.

Central interface of this library is **ApplicationContext**

This allows to store values against named Keys which also store value type information

## Usage

Globally available context in all threads/coroutine

```kotlin
import io.github.funofprograming.context.impl.*

val contextName: String = "TestGlobalApplicationContext"
val valueSetInThread1 = "Value T1"
val validKey: Key<String> = Key.of("ValidKey", String::class.java)

val def1 = launch {
    val globalContext = getGlobalContext(contextName)
    globalContext?.add(validKey, valueSetInThread1)
}

delay(1000)

val def2 = async {
    val globalContext = getGlobalContext(contextName)
    return@async globalContext?.fetch(validKey)
}


println(valueSetInThread1 == def2.await())
```

Above snippet prints:

```kotlin
true
```

ThreadLocal available context in current thread/coroutine

```kotlin
import io.github.funofprograming.context.impl.*

val contextName: String = "TestThreadLocalApplicationContext"
val valueSetInThread1 = "Value T1"
val validKey: Key<String> = Key.of("ValidKey", String::class.java)

val cs1 = initCoroutineScopeForApplicationContext() //notice the initialization of coroutine scope in case we plan to use a thread local context inside a coroutine
val def1 = cs1.async {
    val localContext = getThreadLocalContext(contextName)
    localContext?.add(validKey, valueSetInThread1)
    return@async getThreadLocalContext(contextName)?.fetch(validKey)
}

delay(1000)
val cs2 = initCoroutineScopeForApplicationContext() //notice the initialization of coroutine scope in case we plan to use a thread local context inside a coroutine
val def2 = cs2.async {
    val localContext = getThreadLocalContext(contextName)
    return@async localContext?.fetch(invalidKey)
}

println(def2.await() == null)
println(valueSetInThread1 == def1.await())
```

Above snippet prints:

```kotlin
true
true
```

Also, the context supports restricting keys of context to some predetermined set as follows:

```kotlin
import io.github.funofprograming.context.impl.*

val contextName: String = "TestApplicationContext"
val validKey: Key<String> = Key.of("ValidKey", String::class.java)
val invalidKey: Key<String> = Key.of("InvalidKey", String::class.java)
val permittedKeys: Set<Key<*>> = setOf(validKey)

val globalContext = getGlobalContext(contextName, permittedKeys)
globalContext?.add(validKey, valueSetInThread1)
globalContext?.fetch(invalidKey) //throws InvalidKeyException here since invalidKey is not part of permittedKeys

val localContext = getThreadLocalContext(contextName, permittedKeys)
localContext?.add(validKey, valueSetInThread1)
localContext?.fetch(invalidKey) //throws InvalidKeyException here since invalidKey is not part of permittedKeys
```

## Compatibility matrix

| Application Context Util |  Java  | Kotlin-Std | Kotlin-Coroutines | 
|:------------------------:|:------:|:----------:|:-----------------:|
|          1.0.0           |   21   |  2.1.10    |     1.10.1        |

## LICENSE

Application context util is is released under version 2.0 of the [Apache License](https://www.apache.org/licenses/LICENSE-2.0).