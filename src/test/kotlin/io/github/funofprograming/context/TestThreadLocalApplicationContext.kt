package io.github.funofprograming.context

import io.github.funofprograming.context.impl.*
import kotlinx.coroutines.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*
import java.util.concurrent.ExecutionException
import kotlin.coroutines.EmptyCoroutineContext

class TestThreadLocalApplicationContext {

    private var contextName: String = "TestThreadLocalApplicationContext"
    private var cloneContextName: String = "TestCloneThreadLocalApplicationContext"
    private var validKey: Key<String> = Key.of("ValidKey", String::class.java)
    private var invalidKey: Key<String> = Key.of("InvalidKey", String::class.java)
    private var permittedKeys: Set<Key<*>> = setOf(validKey)

    @AfterEach
    fun tearDown() {
        clearThreadLocalContext(contextName)
    }

    @Test
    @Throws(Throwable::class)
    fun testGetThreadLocalContext():Unit = runBlocking {
        val valueSetInThread1 = "Value T1"

        // testing separate coroutine scopes
        val cs1 = initCoroutineScopeForApplicationContext()
        val def1 = cs1.async {
            val localContext = getThreadLocalContext(contextName)
            localContext?.add(validKey, valueSetInThread1)
            return@async getThreadLocalContext(contextName)?.fetch(validKey)
        }

        delay(1000)
        val cs2 = initCoroutineScopeForApplicationContext()
        val def2 = cs2.async {
            val localContext = getThreadLocalContext(contextName)
            return@async localContext?.fetch(invalidKey)
        }

        assertNull(def2.await())
        assertEquals(valueSetInThread1, def1.await())

        // testing inheritable coroutine scopes
        val cs3 = initCoroutineScopeForApplicationContext()
        val def3 = cs3.async {
            val localContext = getThreadLocalContext(contextName)
            localContext?.add(validKey, valueSetInThread1)
            val cs4 = initCoroutineScopeForApplicationContext(this)
            val def4 = cs4.async {
                val localContext = getThreadLocalContext(contextName)
                return@async localContext?.fetch(validKey)
            }

            return@async def4.await()
        }

        assertEquals(valueSetInThread1, def3.await())
    }

    @Test
    @Throws(Throwable::class)
    fun testGetThreadLocalContextWithPermittedKeys() {
        val valueSetInThread1 = "Value T1"
        val localContext = getThreadLocalContext(contextName, permittedKeys)
        localContext?.add(validKey, valueSetInThread1)
        val permittedKeysInvalid: Set<Key<*>> = setOf(invalidKey)
        assertThrows(InvalidContextException::class.java) { getThreadLocalContext(contextName, permittedKeysInvalid) }
    }

    @Test
    @Throws(Throwable::class)
    fun testGetThreadLocalContextWithPermittedKeysInvalidKey() {
        val valueSetInThread1 = "Value T1"
        val localContext = getThreadLocalContext(contextName, permittedKeys)
        localContext?.add(validKey, valueSetInThread1)
        assertThrows(InvalidKeyException::class.java) { getThreadLocalContext(contextName, permittedKeys)?.fetch(invalidKey) }
    }

    @Test
    @Throws(Throwable::class)
    fun testSetThreadLocalContext() = runBlocking {
        val valueSetInThread1 = "Value T1"
        val cs1 = initCoroutineScopeForApplicationContext()
        val def1 = cs1.async {
            val localContext: ApplicationContext = ApplicationContextImpl(contextName)
            setThreadLocalContext(localContext)
            localContext.add(validKey, valueSetInThread1)
            return@async getThreadLocalContext(contextName)?.fetch(validKey)
        }

        val cs2 = initCoroutineScopeForApplicationContext()
        val def2 = cs2.async {
            val localContext = getThreadLocalContext(contextName)
            return@async localContext?.fetch(validKey)
        }

        delay(1000)

        val def3 = cs1.async {
            return@async getThreadLocalContext(contextName)?.fetch(validKey)
        }

        assertNull(def2.await())
        assertEquals(valueSetInThread1, def1.await())
        assertEquals(valueSetInThread1, def3.await())
    }

    @Test
    @Throws(Throwable::class)
    fun testClearThreadLocalContext() {
        val valueSetInThread1 = "Value T1"
        val localContext: ApplicationContext = ApplicationContextImpl(contextName)
        setThreadLocalContext(localContext)
        localContext.add(validKey, valueSetInThread1)
        assertNotNull(getThreadLocalContext(contextName)?.fetch(validKey))
        clearThreadLocalContext(contextName)
        assertNull(getThreadLocalContext(contextName)?.fetch(validKey))
    }


    @Test
    @Throws(InterruptedException::class, ExecutionException::class)
    fun testCloneThreadLocalContext() {
        val valueSetInThread1 = "Value T1"
        var threadLocalContext = getThreadLocalContext(contextName)
        threadLocalContext?.add(validKey, valueSetInThread1)
        var threadLocalContextClone = threadLocalContext?.clone(cloneContextName)
        assertEquals(valueSetInThread1, threadLocalContextClone?.fetch(validKey))
        clearThreadLocalContext(contextName)

        threadLocalContext = getThreadLocalContext(contextName, permittedKeys)
        threadLocalContext?.add(validKey, valueSetInThread1)
        threadLocalContextClone = threadLocalContext?.clone(cloneContextName, permittedKeys)
        assertEquals(valueSetInThread1, threadLocalContextClone?.fetch(validKey))
    }
}