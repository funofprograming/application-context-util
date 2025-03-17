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
import java.util.concurrent.ExecutionException

class TestGlobalApplicationContext {

    private var contextName: String = "TestGlobalApplicationContext"
    private var cloneContextName: String = "TestCloneGlobalApplicationContext"
    private var validKey1: Key<String> = Key.of<String>("ValidKey1")
    private var validKey2: Key<List<String>> = Key.of<List<String>>("ValidKey2")
    private var invalidKey: Key<String> = Key.of<String>("InvalidKey")
    private var permittedKeys: Set<Key<*>> = setOf(validKey1, validKey2)

    @AfterEach
    fun tearDown() {
        clearGlobalContext(contextName)
    }

    @Test
    fun testGetGlobalContext() = runBlocking {
        val valueSetInThread11 = "Value T1"
        val valueSetInThread12 = listOf("Value T11", "Value T12")

        val def1 = launch {
            val globalContext = getGlobalContext(contextName)
            globalContext?.add(validKey1, valueSetInThread11)
            globalContext?.add(validKey2, valueSetInThread12)
        }

        delay(1000)

        val def2 = async {
            val globalContext = getGlobalContext(contextName)
            return@async globalContext?.fetch(validKey1)
        }

        val def3 = async {
            val globalContext = getGlobalContext(contextName)
            return@async globalContext?.fetch(validKey2)
        }

        assertEquals(valueSetInThread11, def2.await())
        assertEquals(valueSetInThread12, def3.await())
    }

    @Test
    @Throws(Throwable::class)
    fun testGetGlobalContextWithPermittedKeys(): Unit = runBlocking {

        val valueSetInThread1 = "Value T1"

        launch {
            val globalContext = getGlobalContext(contextName, permittedKeys)
            globalContext?.add(validKey1, valueSetInThread1)
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
            globalContext?.add(validKey1, valueSetInThread1)
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

        val valueSetInThread11 = "Value T1"
        val valueSetInThread12 = listOf("Value T11", "Value T12")

        launch {
            val globalContext: ApplicationContext = ConcurrentApplicationContextImpl(contextName)
            setGlobalContext(globalContext)
            globalContext.add(validKey1, valueSetInThread11)
            globalContext.add(validKey2, valueSetInThread12)
        }

        delay(1000)

        val def2 = async {

            val globalContext = getGlobalContext(contextName)
            return@async globalContext?.fetch(validKey1)
        }

        val def3 = async {

            val globalContext = getGlobalContext(contextName)
            return@async globalContext?.fetch(validKey2)
        }

        delay(1000)
        assertEquals(valueSetInThread11, def2.await())
        assertEquals(valueSetInThread12, def3.await())
    }

    @Test
    @Throws(Throwable::class)
    fun testClearGlobalContext(): Unit = runBlocking {

        val valueSetInThread11 = "Value T1"
        val valueSetInThread12 = listOf("Value T11", "Value T12")

        launch {
            val globalContext: ApplicationContext = ConcurrentApplicationContextImpl(contextName)
            setGlobalContext(globalContext)
            globalContext.add(validKey1, valueSetInThread11)
            globalContext.add(validKey2, valueSetInThread12)
        }

        delay(1000)

        launch {
            clearGlobalContext(contextName)
        }

        delay(1000)

        val def2 = async {

            val globalContext = getGlobalContext(contextName)
            return@async globalContext?.fetch(validKey1)
        }

        val def3 = async {

            val globalContext = getGlobalContext(contextName)
            return@async globalContext?.fetch(validKey2)
        }

        delay(1000)
        assertNull(def2.await())
        assertNull(def3.await())
    }

    @Test
    @Throws(InterruptedException::class, ExecutionException::class)
    fun testCloneGlobalContext() {
        val valueSetInThread11 = "Value T1"
        val valueSetInThread12 = listOf("Value T11", "Value T12")
        var globalContext = getGlobalContext(contextName)
        globalContext?.add(validKey1, valueSetInThread11)
        globalContext?.add(validKey2, valueSetInThread12)
        var globalContextClone = globalContext?.clone(cloneContextName)
        assertEquals(valueSetInThread11, globalContextClone?.fetch(validKey1))
        assertEquals(valueSetInThread12, globalContextClone?.fetch(validKey2))
        clearGlobalContext(contextName)

        globalContext = getGlobalContext(contextName, permittedKeys)
        globalContext?.add(validKey1, valueSetInThread11)
        globalContext?.add(validKey2, valueSetInThread12)
        globalContextClone = globalContext?.clone(cloneContextName, permittedKeys)
        assertEquals(valueSetInThread11, globalContextClone?.fetch(validKey1))
        assertEquals(valueSetInThread12, globalContextClone?.fetch(validKey2))
    }

}