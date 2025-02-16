package io.github.funofprograming.context.impl

import io.github.funofprograming.context.ApplicationContext
import io.github.funofprograming.context.ApplicationContextMergeStrategy
import io.github.funofprograming.context.ConcurrentApplicationContext
import io.github.funofprograming.context.Key
import kotlinx.coroutines.runBlocking

open class ConcurrentApplicationContextImpl(private val name: String, private val permittedKeys: Set<Key<*>>? = null) : ApplicationContextImpl(name, permittedKeys), ConcurrentApplicationContext {

    private val concurrencyManager = ContextConcurrencyManager.getInstance()

    override fun <T> addIfNotPresent(key: Key<T>, value: T?) = runBlocking { addIfNotPresent(key, value, null) }

    override suspend fun <T> addIfNotPresent(key: Key<T>, value: T?, timeout: Long?) = concurrencyManager.executeWriteWithLock(timeout) { super.addIfNotPresent(key, value) }

    override fun <T> addWithOverwrite(key: Key<T>, value: T?): Any? = runBlocking { addWithOverwrite(key, value, null) }

    override suspend fun <T> addWithOverwrite(key: Key<T>, value: T?, timeout: Long?): Any? = concurrencyManager.executeWriteWithLock(timeout) { super.addWithOverwrite(key, value) }

    override fun <T> add(key: Key<T>, value: T?) = runBlocking { add(key, value, null) }

    override suspend fun <T> add(key: Key<T>, value: T?, timeout: Long?) = concurrencyManager.executeWriteWithLock(timeout) { super.add(key, value) }

    override fun <T> exists(key: Key<T>): Boolean = runBlocking { exists(key, null) }

    override suspend fun <T> exists(key: Key<T>, timeout: Long?): Boolean = concurrencyManager.executeReadWithLock(timeout) { super.exists(key) }

    override fun <T> fetch(key: Key<T>): T? = runBlocking { fetch(key, null) }

    override suspend fun <T> fetch(key: Key<T>, timeout: Long?): T? = concurrencyManager.executeReadWithLock(timeout) { super.fetch(key) }

    override fun <T> erase(key: Key<T>): T? = runBlocking { erase(key, null) }

    override suspend fun <T> erase(key: Key<T>, timeout: Long?): T? = concurrencyManager.executeWriteWithLock(timeout) { super.erase(key) }

    override fun clear() = runBlocking { clear(null) }

    override suspend fun clear(timeout: Long?) = concurrencyManager.executeWriteWithLock(timeout) { super.clear() }

    override fun merge(other: ApplicationContext, mergeStrategy: ApplicationContextMergeStrategy) = runBlocking { merge(other, mergeStrategy, null) }

    override suspend fun merge(other: ApplicationContext, mergeStrategy: ApplicationContextMergeStrategy, timeout: Long?) = concurrencyManager.executeWriteWithLock(timeout) { super.merge(other, mergeStrategy) }
}