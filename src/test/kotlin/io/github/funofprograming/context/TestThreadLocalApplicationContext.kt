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
    private var validKey1: Key<String> = Key.of<String>("ValidKey1")
    private var validKey2: Key<List<String>> = Key.of<List<String>>("ValidKey2")
    private var invalidKey: Key<String> = Key.of<String>("InvalidKey")
    private var permittedKeys: Set<Key<*>> = setOf(validKey1, validKey2)

    @AfterEach
    fun tearDown() {
        clearThreadLocalContext(contextName)
    }

    @Test
    @Throws(Throwable::class)
    fun testGetThreadLocalContext():Unit = runBlocking {
        val valueSetInThread11 = "Value T1"
        val valueSetInThread12 = listOf("Value T11", "Value T12")

        // testing separate coroutine scopes
        val cs1 = initCoroutineScopeForApplicationContext()
        val def1 = cs1.async {
            val localContext = getThreadLocalContext(contextName)
            localContext?.add(validKey1, valueSetInThread11)
            return@async getThreadLocalContext(contextName)?.fetch(validKey1)
        }

        val cs2 = initCoroutineScopeForApplicationContext()
        val def2 = cs2.async {
            val localContext = getThreadLocalContext(contextName)
            localContext?.add(validKey2, valueSetInThread12)
            return@async getThreadLocalContext(contextName)?.fetch(validKey2)
        }

        delay(1000)
        val cs3 = initCoroutineScopeForApplicationContext()
        val def3 = cs3.async {
            val localContext = getThreadLocalContext(contextName)
            return@async localContext?.fetch(invalidKey)
        }

        assertNull(def3.await())
        assertEquals(valueSetInThread12, def2.await())
        assertEquals(valueSetInThread11, def1.await())

        // testing inheritable coroutine scopes
        val cs4 = initCoroutineScopeForApplicationContext()
        val def4 = cs4.async {
            val localContext = getThreadLocalContext(contextName)
            localContext?.add(validKey1, valueSetInThread11)
            val cs5 = initCoroutineScopeForApplicationContext(this)
            val def5 = cs5.async {
                val localContext = getThreadLocalContext(contextName)
                return@async localContext?.fetch(validKey1)
            }

            return@async def5.await()
        }

        assertEquals(valueSetInThread11, def4.await())
    }

    @Test
    @Throws(Throwable::class)
    fun testGetThreadLocalContextWithPermittedKeys() {
        val valueSetInThread11 = "Value T1"
        val valueSetInThread12 = listOf("Value T11", "Value T12")
        val localContext = getThreadLocalContext(contextName, permittedKeys)
        localContext?.add(validKey1, valueSetInThread11)
        localContext?.add(validKey2, valueSetInThread12)
        val permittedKeysInvalid: Set<Key<*>> = setOf(invalidKey)
        assertThrows(InvalidContextException::class.java) { getThreadLocalContext(contextName, permittedKeysInvalid) }
    }

    @Test
    @Throws(Throwable::class)
    fun testGetThreadLocalContextWithPermittedKeysInvalidKey() {
        val valueSetInThread11 = "Value T1"
        val valueSetInThread12 = listOf("Value T11", "Value T12")
        val localContext = getThreadLocalContext(contextName, permittedKeys)
        localContext?.add(validKey1, valueSetInThread11)
        localContext?.add(validKey2, valueSetInThread12)
        assertThrows(InvalidKeyException::class.java) { getThreadLocalContext(contextName, permittedKeys)?.fetch(invalidKey) }
    }

    @Test
    @Throws(Throwable::class)
    fun testSetThreadLocalContext() = runBlocking {
        val valueSetInThread11 = "Value T1"
        val cs1 = initCoroutineScopeForApplicationContext()
        val def1 = cs1.async {
            val localContext: ApplicationContext = ApplicationContextImpl(contextName)
            setThreadLocalContext(localContext)
            localContext.add(validKey1, valueSetInThread11)
            return@async getThreadLocalContext(contextName)?.fetch(validKey1)
        }

        val cs2 = initCoroutineScopeForApplicationContext()
        val def2 = cs2.async {
            val localContext = getThreadLocalContext(contextName)
            return@async localContext?.fetch(validKey1)
        }

        delay(1000)

        val def3 = cs1.async {
            return@async getThreadLocalContext(contextName)?.fetch(validKey1)
        }

        assertNull(def2.await())
        assertEquals(valueSetInThread11, def1.await())
        assertEquals(valueSetInThread11, def3.await())
    }

    @Test
    @Throws(Throwable::class)
    fun testClearThreadLocalContext() {
        val valueSetInThread11 = "Value T1"
        val valueSetInThread12 = listOf("Value T11", "Value T12")
        val localContext: ApplicationContext = ApplicationContextImpl(contextName)
        setThreadLocalContext(localContext)
        localContext.add(validKey1, valueSetInThread11)
        localContext.add(validKey2, valueSetInThread12)
        assertNotNull(getThreadLocalContext(contextName)?.fetch(validKey1))
        assertNotNull(getThreadLocalContext(contextName)?.fetch(validKey2))
        clearThreadLocalContext(contextName)
        assertNull(getThreadLocalContext(contextName)?.fetch(validKey1))
        assertNull(getThreadLocalContext(contextName)?.fetch(validKey2))
    }


    @Test
    @Throws(InterruptedException::class, ExecutionException::class)
    fun testCloneThreadLocalContext() {
        val valueSetInThread11 = "Value T1"
        val valueSetInThread12 = listOf("Value T11", "Value T12")
        var threadLocalContext = getThreadLocalContext(contextName)
        threadLocalContext?.add(validKey1, valueSetInThread11)
        threadLocalContext?.add(validKey2, valueSetInThread12)
        var threadLocalContextClone = threadLocalContext?.clone(cloneContextName)
        assertEquals(valueSetInThread11, threadLocalContextClone?.fetch(validKey1))
        assertEquals(valueSetInThread12, threadLocalContextClone?.fetch(validKey2))
        clearThreadLocalContext(contextName)

        threadLocalContext = getThreadLocalContext(contextName, permittedKeys)
        threadLocalContext?.add(validKey1, valueSetInThread11)
        threadLocalContext?.add(validKey2, valueSetInThread12)
        threadLocalContextClone = threadLocalContext?.clone(cloneContextName, permittedKeys)
        assertEquals(valueSetInThread11, threadLocalContextClone?.fetch(validKey1))
        assertEquals(valueSetInThread12, threadLocalContextClone?.fetch(validKey2))
    }
}