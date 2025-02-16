package io.github.funofprograming.context

import io.github.funofprograming.context.impl.*
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.*

class TestGlobalApplicationContext {

    private var contextName: String = "TestThreadLocalApplicationContext"
    private var validKey: Key<String> = Key.of("ValidKey", String::class.java)
    private var invalidKey: Key<String> = Key.of("InvalidKey", String::class.java)
    private var permittedKeys: Set<Key<*>> = setOf(validKey)

    @AfterEach
    fun tearDown() {
        clearGlobalContext(contextName)
    }

    @Test
    fun testGetGlobalContext() = runBlocking {
        val valueSetInThread1 = "Value T1"

        val def1 = launch {
            val globalContext = getGlobalContext(contextName)
            globalContext?.add(validKey, valueSetInThread1)
        }

        delay(1000)

        val def2 = async {
            val globalContext = getGlobalContext(contextName)
            return@async globalContext?.fetch(validKey)
        }


        assertEquals(valueSetInThread1, def2.await())
    }

    @Test
    @Throws(Throwable::class)
    fun testGetGlobalContextWithPermittedKeys(): Unit = runBlocking {

        val valueSetInThread1 = "Value T1"

        launch {
            val globalContext = getGlobalContext(contextName, permittedKeys)
            globalContext?.add(validKey, valueSetInThread1)
        }

        delay(1000)

        val def2 = async {
            val permittedKeysInvalid = setOf(invalidKey)
            assertThrows(InvalidContextException::class.java) { getGlobalContext(contextName, permittedKeysInvalid) }
        }

        delay(1000)
        def2.await()
    }

    @Test
    @Throws(Throwable::class)
    fun testGetGlobalContextWithPermittedKeysInvalidKey(): Unit = runBlocking {

        val valueSetInThread1 = "Value T1"

        launch {
            val globalContext = getGlobalContext(contextName, permittedKeys)
            globalContext?.add(validKey, valueSetInThread1)
        }

        delay(1000)

        val def2 = async {

            val globalContext = getGlobalContext(contextName, permittedKeys)
            assertThrows(InvalidKeyException::class.java) { globalContext?.fetch(invalidKey) }
        }

        delay(1000)
        def2.await()
    }

    @Test
    @Throws(Throwable::class)
    fun testSetGlobalContext(): Unit = runBlocking {

        val valueSetInThread1 = "Value T1"

        launch {
            val globalContext: ApplicationContext = ConcurrentApplicationContextImpl(contextName, null)
            setGlobalContext(globalContext)
            globalContext.add(validKey, valueSetInThread1)
        }

        delay(1000)

        val def2 = async {

            val globalContext = getGlobalContext(contextName)
            return@async globalContext?.fetch(validKey)
        }

        delay(1000)
        assertEquals(valueSetInThread1, def2.await())
    }

    @Test
    @Throws(Throwable::class)
    fun testClearGlobalContext(): Unit = runBlocking {

        val valueSetInThread1 = "Value T1"

        launch {
            val globalContext: ApplicationContext = ConcurrentApplicationContextImpl(contextName, null)
            setGlobalContext(globalContext)
            globalContext.add(validKey, valueSetInThread1)
        }

        delay(1000)

        launch {
            clearGlobalContext(contextName)
        }

        delay(1000)

        val def2 = async {

            val globalContext = getGlobalContext(contextName)
            return@async globalContext?.fetch(validKey)
        }

        delay(1000)
        assertNull(def2.await())
    }

}